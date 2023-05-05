from fastapi import APIRouter
from ..database.MongoDB import MongoDB
from ..model.login import LoginDTO
from ..model.registerDTO import RegisterDTO
from ..model.UserDTO import UserInformationDTO

mongo_client = MongoDB()
database = mongo_client.get_mongo_client("SupperMarketApplication")
user_collection = database["user"]

router = APIRouter(
    prefix="/user",
    responses={404: {"description": "Not found"}},
)


def is_exists_user(username: str):
    is_exist_user_query = {"username": username}
    is_exist_user = user_collection.find_one(is_exist_user_query, {"_id": 0})
    return is_exist_user != None


def authentication_user(username: str, password: str):
    query = {"username": username, "password": password}
    user = user_collection.find_one(query, {"_id": 0})
    return user != None


@router.post("/login")
async def login(loginDTO: LoginDTO):
    username = loginDTO.username
    password = loginDTO.password
    if not is_exists_user(username):
        return {"status": False, "message": "User is not exits"}

    if not authentication_user(username, password):
        return {"status": False, "message": "Wrong username/password"}

    return {"status": True, "message": "Login successfully"}


@router.post("/information")
async def information(userInformationDTO: UserInformationDTO):
    username = userInformationDTO.username
    if not is_exists_user(username):
        return {"status": False, "message": "User is not exits"}

    information_query = {"username": username}
    user_information = user_collection.find_one(information_query, {"_id": 0})
    status = user_information != None
    return {"status": status, "data": user_information}


@router.post("/register")
async def register(registerDTO: RegisterDTO):
    username = registerDTO.username
    first_name = registerDTO.first_name
    last_name = registerDTO.last_name
    phone_number = registerDTO.phone_number
    password = registerDTO.password
    gender = registerDTO.gender
    city = registerDTO.city
    district = registerDTO.district
    ward = registerDTO.ward
    address = registerDTO.address
    type_of_address = registerDTO.type_of_address
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
    return {"status": True, "message": "Register successfully"}
