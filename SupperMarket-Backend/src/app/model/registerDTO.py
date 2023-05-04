from pydantic import BaseModel


class RegisterDTO(BaseModel):
    username: str
    first_name: str
    last_name: str
    phone_number: str
    password: str
    gender: str
    city: str
    district: str
    ward: str
    address: str
    type_of_address: str
