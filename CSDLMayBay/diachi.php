<?php
class DiaChi{
    function __construct($id,$tenDiaDiem,$maDiaDiem,$tenSanBay){
        $this->Id = $id;
        $this->TenDiaDiem = $tenDiaDiem;
        $this->MaDiaDiem = $maDiaDiem;
        $this->TenSanBay = $tenSanBay;
    }
}
$danhSachDiaDiem=array();
$connect=new mysqli("localhost","root","","quanlymaybay");
if($connect->connect_error) {
    die("ket not that bai". $connect->connect_error);
}
$method = $_SERVER['REQUEST_METHOD'];

switch ($method) {
    case 'GET':
        $connect->set_charset("utf8");
        if(isset($_GET["me"])) {
            $tenTinhDi=$_GET['tenTinhDi'];
            $tenTinhDen=$_GET['tenTinhDen'];
            $me=$_GET['me'];
            switch ($me) {
                case 'timdiachi':
                    $query="SELECT*FROM diadiem WHERE tenTinh='$tenTinhDi' OR tenTinh='$tenTinhDen'";
                    break;
                default:
                    $query="SELECT*FROM diadiem";
                    break;
            }
        }else{
            $query="SELECT*FROM diadiem";
        }
        $data=mysqli_query( $connect,$query);
        while($row=mysqli_fetch_assoc($data)){
            array_push( $danhSachDiaDiem,new DiaChi($row['id'],$row['tenTinh'], $row['maTinh'],$row['tenSanBay']));
        }
        echo json_encode($danhSachDiaDiem);
        break;
    case 'POST':
        $meth=$_POST['ME'];

        $id=$_POST['id'];
        $tenDiaDiem=$_POST['tenDiaDiem'];
        $maDiaDiem=$_POST['maDiaDiem'];
        $tenSanBay=$_POST['tenSanBay']; 

        $id=$connect->real_escape_string($id);
        $tenDiaDiem=$connect->real_escape_string($tenDiaDiem);
        $maDiaDiem=$connect->real_escape_string($maDiaDiem);
        $tenSanBay=$connect->real_escape_string($tenSanBay);
        switch ($meth) {
            case 'insert':
                $query="INSERT INTO diadiem VALUES (null,'$tenDiaDiem','$maDiaDiem','$tenSanBay')";
                if($connect->query($query)==TRUE) {
                    echo "ThanhCong";
                }else{
                    echo "ThatBai";
                }
                $connect->close();
                break;
            case 'update':
                $query= "UPDATE diadiem SET tenTinh='$tenDiaDiem',maTinh='$maDiaDiem',tenSanBay='$tenSanBay'WHERE id='$id'";
                if($connect->query($query)==TRUE) {
                    echo "ThanhCong";
                }else{
                    echo "ThatBai";
                }
                $connect->close();
                break;  
            case 'delete':
                $query="DELETE FROM diadiem WHERE id='$id'";
                if($connect->query($query)==TRUE) {
                    echo "ThanhCong";
                }else{
                    echo "ThatBai";
                }
                $connect->close();
                break;  
        }
        break;
    default:
        http_response_code(405);
        echo json_encode(array("message" => "Method not allowed"));
        break;
    }
?>