from fastapi import APIRouter
from ..database.MongoDB import MongoDB
from datetime import datetime
from ..model.CartDTO import AddToCart
from ..model.UserDTO import UserInformationDTO

mongo_client = MongoDB()
database = mongo_client.get_mongo_client("SupperMarketApplication")
receipt_collection = database["receipt"]
receipt_line_collection = database["receiptLine"]
product_collection = database["products"]
users_collection = database["user"]

router = APIRouter(
    prefix="/cart",
    responses={404: {"description": "Not found"}},
)


@router.post("/add-to-cart")
async def add_to_cart(addToCartDTO: AddToCart):
    username = addToCartDTO.username
    product_id = addToCartDTO.product_id
    quantity = addToCartDTO.quantity

    current_receipt = receipt_collection.find_one(
        {"username": username, "state": "unpaid"}, {"_id": 0}
    )

    receipt_id = ""

    if current_receipt == None:
        index = len(
            [receipt for receipt in receipt_collection.find({}, {"_id": 0})]
        )

        receipt_id = (
            f"{datetime.now().strftime('%d%m%Y')}{str(index + 1).zfill(3)}"
        )

        receipt_new = {
            "id": receipt_id,
            "username": username,
            "total": 0,
            "state": "unpaid",
            "dateEstablished": datetime.now().strftime("%m/%d/%Y %H:%M:%S"),
        }

        receipt_collection.insert_one(receipt_new)
    else:
        receipt_id = current_receipt["id"]

    product_price = product_collection.find_one(
        {"id": product_id}, {"_id": 0}
    )["price"]

    exist_receipt = receipt_line_collection.find_one(
        {"id": receipt_id, "product_id": product_id}, {"_id": 0}
    )

    if exist_receipt != None:
        receipt_line_index = {
            "$set": {
                "quantity": exist_receipt["quantity"] + quantity,
                "price": exist_receipt["price"] + product_price * quantity,
            }
        }

        receipt_line_collection.update_one(
            {"id": receipt_id, "product_id": product_id}, receipt_line_index
        )
    else:
        receipt_line_index = {
            "id": receipt_id,
            "username": username,
            "product_id": product_id,
            "quantity": quantity,
            "price": product_price * quantity,
        }

        receipt_line_collection.insert_one(receipt_line_index)

    all_receipt_line = receipt_line_collection.find(
        {"id": receipt_id}, {"_id": 0}
    )
    total = 0
    for receipt_line in all_receipt_line:
        total += receipt_line["price"]

    receipt_index = {"$set": {"total": total}}
    receipt_collection.update_one({"id": receipt_id}, receipt_index)

    return {"status": True, "data": "Add product successfully"}


@router.post("/own-cart")
async def get_cart(userInformationDTO: UserInformationDTO):
    username = userInformationDTO.username
    current_receipt = receipt_collection.find_one(
        {"username": username, "state": "unpaid"}, {"_id": 0}
    )

    if current_receipt == None:
        return {"status": False, "message": "Cart is empty"}

    receipt_id = current_receipt["id"]
    total = current_receipt["total"]
    cart = receipt_line_collection.find({"id": receipt_id}, {"_id": 0})
    user_cart = []
    for product in cart:
        product_id = product["product_id"]
        product_in_cart = product_collection.find_one(
            {"id": product_id}, {"_id": 0}
        )
        user_cart.append(
            {
                "product": product_in_cart,
                "quantity": product["quantity"],
                "price": product["price"],
            }
        )

    status = user_cart != None
    return {
        "status": status,
        "receipt_id": receipt_id,
        "total": total,
        "cart": user_cart,
    }


@router.post("/update-quantity")
async def update_quantity(updateQuantity: AddToCart):
    username = updateQuantity.username
    product_id = updateQuantity.product_id
    quantity = updateQuantity.quantity

    current_receipt = receipt_collection.find_one(
        {"username": username, "state": "unpaid"}, {"_id": 0}
    )

    receipt_id = current_receipt["id"]

    product_price = product_collection.find_one(
        {"id": product_id}, {"_id": 0}
    )["price"]

    exist_receipt = receipt_line_collection.find_one(
        {"id": receipt_id, "product_id": product_id}, {"_id": 0}
    )

    receipt_line_index = {
        "$set": {
            "quantity": quantity,
            "price": product_price * quantity,
        }
    }

    receipt_line_collection.update_one(
        {"id": receipt_id, "product_id": product_id}, receipt_line_index
    )

    all_receipt_line = receipt_line_collection.find(
        {"id": receipt_id}, {"_id": 0}
    )

    total = 0
    for receipt_line in all_receipt_line:
        total += receipt_line["price"]

    receipt_index = {"$set": {"total": total}}
    receipt_collection.update_one({"id": receipt_id}, receipt_index)

    if quantity == 0:
        receipt_line_collection.delete_one(
            {"id": receipt_id, "product_id": product_id}
        )

    return {"state": True, "message": "Update successfully"}


@router.post("/history")
async def get_history(userInformationDTO: UserInformationDTO):
    username = userInformationDTO.username
    receipts = receipt_collection.find(
        {"username": username, "state": "pay"}, {"_id": 0}
    )

    history_transactions = []

    for receipt in receipts:
        receipt_id = receipt["id"]
        cart = receipt_line_collection.find({"id": receipt_id}, {"_id": 0})
        for product in cart:
            product_id = product["product_id"]
            product_in_cart = product_collection.find_one(
                {"id": product_id}, {"_id": 0}
            )
            history_transactions.append(
                {
                    "product": product_in_cart,
                    "receipt_id": receipt_id,
                    "quantity": product["quantity"],
                    "price": product["price"],
                }
            )

    status = history_transactions != []
    return {
        "status": status,
        "history": history_transactions,
    }


@router.post("/checkout")
async def checkout(userInformationDTO: UserInformationDTO):
    username = userInformationDTO.username
    receipt = {"username": username, "state": "unpaid"}
    user = users_collection.find_one({"username": username}, {"_id": 0})
    user_receipt = receipt_collection.find_one(receipt)
    
    if user_receipt["total"] <= user["balance"]:
        update_state = {"$set": {"state": "pay"}}
        receipt_collection.update_one(receipt, update_state)
        return {"status": True, "message": "Checkout successfully"}
    
    return {"status": False, "message": "Checkout unsuccessfully"}
