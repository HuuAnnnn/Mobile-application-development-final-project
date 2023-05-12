from pydantic import BaseModel


class UserInformationDTO(BaseModel):
    username: str


class ChangePasswordDTO(BaseModel):
    username: str
    newPassword: str


class UpdateAddressDTO(BaseModel):
    username: str
    city: str
    district: str
    ward: str
    address: str
    type_of_address: str


class UpdateUserImageDTO(BaseModel):
    username: str
    newImage: str


class UpdateBalanceDTO(BaseModel):
    username: str
    amount: float
