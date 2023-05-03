from fastapi import APIRouter
from ..database.MongoDB import MongoDB

mongo_client = MongoDB()
database = mongo_client.get_mongo_client("SupperMarketApplication")
user_collection = database["user"]

router = APIRouter(
    prefix="/user",
    responses={404: {"description": "Not found"}},
)


def is_exists_user(username: str):
    is_exist_user_query = {"username": username}
    is_exist_user = user_collection.find(is_exist_user_query, {"_id": 0})
    return is_exist_user != None


def authentication_user(username: str, password: str):
    query = {"username": username, "password": password}
    user = user_collection.find_one(query, {"_id": 0})
    return user != None


@router.post("/login")
async def login(username: str, password: str):
    if not is_exists_user(username):
        return {"status:": False, "message": "User is not exits"}

    if not authentication_user(username, password):
        return {"status:": False, "message": "Wrong username/password"}

    return {"status:": True, "message": "Login successfully"}


@router.post("/register")
async def register(
    username: str,
    first_name: str,
    last_name: str,
    phone_number: str,
    password: str,
    gender: str,
    city: str,
    district: str,
    ward: str,
    address: str,
    type_of_address: str = "1",
):
    if is_exists_user(username):
        return {"status": False, "message": "User is already exists"}

    user_information = {
        "username": username,
        "first_name": first_name,
        "last_name": last_name,
        "phone_number": phone_number,
        "password": password,
        "gender": gender,
        "city": city,
        "district": district,
        "ward": ward,
        "address": address,
        "type_of_address": type_of_address,
    }

    user_collection.insert_one(user_information)
    return {"status:": True, "message": "Register successfully"}
