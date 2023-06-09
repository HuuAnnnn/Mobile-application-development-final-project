import base64
from fastapi import APIRouter
import requests
from ..database.MongoDB import MongoDB
from ..model.notification import NotificationDTO
from datetime import datetime
import pymongo

mongo_client = MongoDB()
database = mongo_client.get_mongo_client("SupperMarketApplication")
notification_collection = database["notification"]

router = APIRouter(
    prefix="/notification",
    responses={404: {"description": "Not found"}},
)


@router.post("/add-new-notification")
async def add_new_post(notificationDTO: NotificationDTO):
    index = len(
        [
            notification
            for notification in notification_collection.find({}, {"_id": 0})
        ]
    )
    id = f"{datetime.now().strftime('%d%m%Y')}{(index + 1)}"
    index = {
        "id": id,
        "title": notificationDTO.title,
        "content": notificationDTO.content,
        "image": get_as_base64(notificationDTO.image_url),
        "dateCreate": datetime.now().strftime("%d/%m/%Y %H:%M:%S"),
    }

    notification_collection.insert_one(index)


@router.get("/notifications")
async def notifications():
    notifications = notification_collection.find({}, {"_id": 0}).sort(
        "dateCreate", pymongo.DESCENDING
    )

    data = []
    for notification in notifications:
        data.append(notification)
    status = data != None
    return {"status": status, "data": data}


def get_as_base64(url):
    return base64.b64encode(requests.get(url).content)


@router.get("/get-notification")
async def get_notification(notificationID: str):
    notifications = notification_collection.find_one(
        {"id": notificationID}, {"_id": 0}
    )

    status = notifications != None
    return {"status": status, "data": notifications}


def get_as_base64(url):
    return base64.b64encode(requests.get(url).content)
