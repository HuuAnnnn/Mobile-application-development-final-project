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

    return {"state": True, "message": "Update successfully"}
