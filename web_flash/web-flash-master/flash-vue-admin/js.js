if ($明细表1.明细表可能为空$.size() > 0) { 
    for (var i = 0; i < $明细表1.明细表可能为空$.size(); i++) {
         if ("".equals($列表.获取某一行的值$($明细表1.明细表可能为空$, i))) { 
            return true; 
        } 
    } 
    return false;
 }else{
    return true; 
 }