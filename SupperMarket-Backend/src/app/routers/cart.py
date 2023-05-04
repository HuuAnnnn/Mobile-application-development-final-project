from fastapi import APIRouter
from ..database.MongoDB import MongoDB
from pprint import PrettyPrinter

mongo_client = MongoDB()
database = mongo_client.get_mongo_client("SupperMarketApplication")
cart_collection = database["cart"]
receipt_collection = database["receipt"]
receipt_line_collection = database["receiptLine"]

router = APIRouter(
    prefix="/cart",
    responses={404: {"description": "Not found"}},
)


@router.post("/add-to-cart")
async def add_to_cart(username: str, product_id: str):