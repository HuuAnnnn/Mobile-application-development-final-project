from fastapi import APIRouter

router = APIRouter(
    responses={404: {"description": "Not found"}},
)


@router.get("/users", tags=["users"])
async def read_users():
    return [{"username": "An"}, {"password": "123"}]
