<?php
class KhachHang{
    public function __construct($id,$hoVaTen,$gmail,$matKhau,$diaChi,$soDienThoai,$cccd){
        $this->Id = $id;
        $this->HoVaTen = $hoVaTen;
        $this->Gmail = $gmail;
        $this->MatKhau = $matKhau;
        $this->DiaChi= $diaChi;
        $this->SoDienThoai = $soDienThoai;
        $this->CCCD = $cccd;
    }
}
$danhSachKhachHang=array();
$connect=new mysqli("localhost","root","","quanlymaybay");
if($connect->connect_error) {
    die("ket noi that bai". $connect->connect_error);
}
$method = $_SERVER['REQUEST_METHOD'];
switch($method) {
    case 'GET': {
        $connect->set_charset('utf8');
        if(isset($_GET['me'])) {
            $me = $_GET['me'];
            $gmail = $_GET['gmail'];
            $matKhau = $_GET['matKhau'];
            switch($me) {
                case 'timkhachhang': {
                    $query = "SELECT * FROM khachhang WHERE gmail='$gmail' AND matKhau='$matKhau'";
                    break;
                }
                case 'timkhachhang2':{
                    $query = "SELECT * FROM khachhang WHERE gmail='$gmail'";
                    break;
                }
                case "kiemtravadangnhap": {
                    $query = "SELECT * FROM khachhang WHERE gmail='$gmail' AND matKhau='$matKhau'";
                    break;
                }
                default: {
                    $query = "SELECT * FROM khachhang";
                    break;
                }
            }
        } else {
            $query = "SELECT * FROM khachhang";
        }
        $data = mysqli_query($connect, $query);
        while($row = mysqli_fetch_array($data)) {
            array_push($danhSachKhachHang, new KhachHang($row['id'], $row['hoVaTen'], $row['gmail'], $row['matKhau'], $row['diaChi'], $row['soDienThoai'], $row['cCCD']));
        }
        echo json_encode($danhSachKhachHang);
        break;
    }
    case 'POST':{
        $diaChi=$_POST['diaChi'];
        $meth=$_POST['ME'];
        $id=$_POST['id'];
        $matKhau=$_POST['matKhau'];
        $hoVaTen=$_POST['hoVaTen'];
        $gmail=$_POST['gmail'];
        $soDienThoai=$_POST['soDienThoai'];
        $cCCD=$_POST['cCCD'];
        switch($meth) {
            case 'insert':{
                $query= "INSERT INTO khachhang VALUES (null,'$hoVaTen','$gmail','$matKhau','$diaChi','$soDienThoai','$cCCD')";
                if($connect->query($query)==TRUE) {
                    echo "1";
                }else{
                    echo "0";
                }
                $connect->close();
                break;
            }
            case 'update':{
                $query= "UPDATE khachhang SET hoVaTen='$hoVaTen',diaChi='$diaChi',soDienThoai='$soDienThoai' WHERE id ='$id'";
                if($connect->query($query)==TRUE) {
                    echo "1";
                }else{
                    echo "0";
                }
                $connect->close();
                break;
            }
            case 'delete':{
                $query= "DELETE FROM khachhang WHERE id='$id'";
                if($connect->query($query)==TRUE) {
                    echo "1";
                }else{
                    echo "0";
                }
                $connect->close();
                break;
            }
            case 'doiMatKhau':{
                $queryLayMatKhai="SELECT * FROM khachhang WHERE id='$id'";
                $data=mysqli_query($connect,$queryLayMatKhai);
                $matKhauHienTai;
                while ($row=mysqli_fetch_array($data)) {
                    $matKhauHienTai=$row['matKhau'];
                }
                if($matKhau==$matKhauHienTai){
                    $matKhauMoi=$_POST['matKhauMoi'];
                    $query="UPDATE khachhang SET matKhau='$matKhauMoi' WHERE id ='$id'";
                    if($connect->query($query)==TRUE) {
                        echo "1";
                    }else{
                        echo "0";
                    }
                    $connect->close();
                }else{
                    echo "2";
                }
                break;
            }
            default:{
                break;
            }
        }
        break;
    }
    default:
        http_response_code(405);
        echo json_encode(array("message" => "Method not allowed"));
        break;
}
?>
