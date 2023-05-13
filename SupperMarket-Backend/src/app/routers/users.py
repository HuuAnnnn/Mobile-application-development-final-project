from fastapi import APIRouter
from ..database.MongoDB import MongoDB
from ..model.login import LoginDTO
from ..model.registerDTO import RegisterDTO
from ..model.UserDTO import (
    UserInformationDTO,
    ChangePasswordDTO,
    UpdateAddressDTO,
    UpdateUserImageDTO,
    UpdateBalanceDTO,
    UpdateInformationDTO,
)

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


@router.post("/update-user-image")
async def update_user_image(updateUserImage: UpdateUserImageDTO):
    username = updateUserImage.username
    image = updateUserImage.newImage

    if is_exists_user(username):
        user_collection.update_one(
            {"username": username}, {"$set": {"image": image}}
        )
        return {"status": True, "message": "Update successfully"}

    return {"status": False, "message": "User is not exits"}


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
        "balance": 0,
        "image": "",
        "district": district,
        "ward": ward,
        "address": address,
        "type_of_address": type_of_address,
    }

    user_collection.insert_one(user_information)
    return {"status": True, "message": "Register successfully"}


@router.post("/change-password")
async def change_password(changePassword: ChangePasswordDTO):
    username = changePassword.username
    new_password = changePassword.newPassword

    query = {"username": username}
    update_password = {"$set": {"password": new_password}}

    user_collection.update_one(query, update_password)

    return {"status": True, "message": "Update password successfully"}


@router.post("/change-address")
async def register(updateAddressDTO: UpdateAddressDTO):
    username = updateAddressDTO.username
    city = updateAddressDTO.city
    district = updateAddressDTO.district
    ward = updateAddressDTO.ward
    address = updateAddressDTO.address
    type_of_address = updateAddressDTO.type_of_address

    if not is_exists_user(username):
        return {"status": False, "message": "User is not exists"}

    address_update = {
        "city": city,
        "district": district,
        "ward": ward,
        "address": address,
        "type_of_address": type_of_address,
    }

    user_collection.update_one(
        {"username": username}, {"$set": address_update}
    )

    return {"status": True, "message": "Register successfully"}


@router.post("/update-information")
async def update_user_information(updateInformation: UpdateInformationDTO):
    username = updateInformation.username
    first_name = updateInformation.first_name
    last_name = updateInformation.last_name
    phone_number = updateInformation.phone_number
    gender = updateInformation.gender
    city = updateInformation.city
    district = updateInformation.district
    ward = updateInformation.ward
    address = updateInformation.address

    if is_exists_user(username):
        user_query = {"username": username}
        update_info = {
            "$set": {
                "first_name": first_name,
                "last_name": last_name,
                "phone_number": phone_number,
                "gender": gender,
                "city": city,
                "district": district,
                "ward": ward,
                "address": address,
            }
        }

        user_collection.update_one(user_query, update_info)
        return {"status": True, "message": "Update successfully"}

    return {"status": False, "message": "User is not exists"}


@router.post("/update-balance")
async def update_balance(updateBalanceDTO: UpdateBalanceDTO):
    username = updateBalanceDTO.username
    new_money = updateBalanceDTO.amount
    if is_exists_user(username=username):
        find_user = {"username": username}
        user = user_collection.find_one(find_user)
        user_balance = user["balance"]
        new_balance = new_money + user_balance
        user_collection.update_one(
            find_user,
            {"$set": {"balance": new_balance}},
        )

        return {"status": True, "message": "Charge money successfully"}

    return {"status": False, "message": "User is not exists"}
