
// types/room.ts
export interface RoomItem {
    fdId: string
    id: number,
    createTime: number,
    createBy: number,
    modifyTime: number,
    modifyBy: string,
    fdName: string,
    fdMassage: string,
    fdBz: string,
    fdType: string,
    fdUserName: string,
    fdActoinTime: number,
    fdEndTime: number,
    richTextContent_long: string,
    fileList: [],
    fileLists: [],
    fileListss: []
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


