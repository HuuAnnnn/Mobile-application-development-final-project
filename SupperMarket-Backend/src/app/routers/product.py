from fastapi import APIRouter
from ..database.MongoDB import MongoDB
from pprint import PrettyPrinter

mongo_client = MongoDB()
database = mongo_client.get_mongo_client("SupperMarketApplication")
product_collection = database["products"]

router = APIRouter(
    prefix="/product",
    responses={404: {"description": "Not found"}},
)


@router.get("/products")
async def get_products():
    products = product_collection.find({}, {"_id": 0})
    data = []
    for product in products:
        data.append(product)

    status = data != None
    return {"status": status, "data": data}


@router.post("/products")
async def get_product_by_category(category: str):
    category_query = {"category": category}
    products_category = product_collection.find(category_query, {"_id": 0})
    data = []
    for product in products_category:
        data.append(product)
    status = data != None
    return {"status": status, "data": data}


@router.get("/search")
async def search(keyword: str):
    result = product_collection.aggregate(
        [
            {
                "$search": {
                    "index": "product_name_search",
                    "text": {
                        "query": keyword,
                        "path": "name",
                        "fuzzy": {},
                    },
                }
            },
            {"$project": {"_id": 0}},
        ]
    )

    data = []
    for product in result:
        data.append(product)

    return data
