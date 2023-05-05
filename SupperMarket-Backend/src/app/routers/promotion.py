from fastapi import APIRouter
from ..database.MongoDB import MongoDB
from datetime import datetime
from ..model.PromotionDTO import PromotionDTO

mongo_client = MongoDB()
database = mongo_client.get_mongo_client("SupperMarketApplication")
promotion_collection = database["promotion"]

router = APIRouter(
    prefix="/promotion",
    responses={404: {"description": "Not found"}},
)


@router.get("/promotions")
async def get_promotions():
    promotions = promotion_collection.find({}, {"_id": 0})
    data = [promotion for promotion in promotions]

    status = data != []

    return {"status": status, "data": data}


@router.get("/add-promotion")
async def insert_promotions(promotionDTO: PromotionDTO):
    title = promotionDTO.title
    dateExpired = promotionDTO.dateExpired
    code = promotionDTO.code

    promotion = {
        "title": title,
        "dateExpired": dateExpired,
        "code": code,
    }

    promotion_collection.insert_one(promotion)

    return {"status": True, "message": "Insert successfully"}
