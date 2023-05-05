from pydantic import BaseModel

class UserInformationDTO(BaseModel):
  username : str