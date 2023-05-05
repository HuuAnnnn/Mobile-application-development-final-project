from pydantic import BaseModel


class UserInformationDTO(BaseModel):
    username: str


class ChangePasswordDTO(BaseModel):
    username: str
    newPassword: str
