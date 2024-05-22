<?php
class MayBay{
    function __construct($id,$maMayBay,$tenMayBay,$soGhe){
        $this->Id=$id;
        $this->MaMayBay=$maMayBay;
        $this->TenMayBay=$tenMayBay;
        $this->SoGhe=$soGhe;
    }
}
$danhSachMayBay=array();
$connect=new mysqli("localhost","root","","quanlymaybay");
if($connect->connect_error) die("Ket noi that bai".$connect->connect_error);
$method=$_SERVER['REQUEST_METHOD'];

switch ($method) {
    case 'GET':
        $connect->set_charset("utf8");
        if(isset($_GET["me"])){
            $tenMayBay=$_GET["tenMayBay"];
            $me=$_GET["me"];
            switch ($me) {
                case 'timmaybay':
                    $query="SELECT * FROM maybay WHERE tenMayBay='$tenMayBay'";
                    break;
                default:
                $query="SELECT*FROM maybay";
                break;
            }
        }else{
            $query="SELECT*FROM maybay";

        };
        $data=mysqli_query( $connect,$query);
        while($row=mysqli_fetch_assoc($data)){
            array_push( $danhSachMayBay,new MayBay($row['id'],$row['maMayBay'], $row['tenMayBay'],$row['soGhe']));
        }
        echo json_encode($danhSachMayBay);
        break;
    case 'POST':
        $meth=$_POST['ME'];
        $id=$_POST['id'];
        $maMayBay=$_POST['maMayBay'];
        $tenMayBay=$_POST['tenMayBay'];
        $soGhe=$_POST['soGhe']; 
        
        $id=$connect->real_escape_string($id);
        $maMayBay=$connect->real_escape_string($maMayBay);
        $tenMayBay=$connect->real_escape_string($tenMayBay);
        $soGhe=$connect->real_escape_string($soGhe);
        switch ($meth) {
            case 'insert':
                $query="INSERT INTO maybay VALUES (null,'$maMayBay','$tenMayBay','$soGhe')";
                if($connect->query($query)==TRUE) {
                    echo "ThanhCong";
                }else{
                    echo "ThatBai";
                }
                $connect->close();
                break;
            case 'update':
                $query= "UPDATE maybay SET maMayBay='$maMayBay',tenMayBay='$tenMayBay',soGhe='$soGhe'WHERE id='$id'";
                if($connect->query($query)==TRUE) {
                    echo "ThanhCong";
                }else{
                    echo "ThatBai";
                }
                $connect->close();
                break;  
            case 'delete':
                $query="DELETE FROM maybay WHERE id='$id'";
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