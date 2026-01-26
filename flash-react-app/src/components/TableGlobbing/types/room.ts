
// types/room.ts
export interface RoomItem {
    id: string;
    createTime: string;
    createBy: number;
    modifyTime: string;
    fdType: string;
    fdName: string;
    fdMassage: string;
    fdCode:string;
    fdUserName:string;
    fdActoinTime:string;
    fdEndTime:string;
    img:string;

    fdbz: string;
}

export interface ApiResponse {
    code: number;
    msg: string;
    data: RoomItem[];
    success: boolean;
}

export interface ColDate {
    title: string;
    dataIndex: string;
    key: string;
    width: number;
    sorter:any
}
export interface CreateRoomDto {
    fdName: string;
    fdRoomName: string;
    fdRoomPhone: string;
    fdAddres: string;
    fdPrincipal: string;
    fdIsWhole: string;
    fdAbcde: string;
    fdlease: string;
    fdPrincipalPhone: string;
    img: string;
    fdbz: string;
}