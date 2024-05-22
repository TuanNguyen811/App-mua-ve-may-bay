<?php
class VeMayBayDaBan{
    function __construct($id,$maChuyenBay,$soGhe,$gmailKhachHang,$tenKhachHang,$sdtKhachHang,$tinhDi,$tinhDen,$ngayDi,$giaVe,$maMayBay){
        $this->Id = $id;
        $this->MaChuyenBay = $maChuyenBay;
        $this->SoGhe = $soGhe;
        $this->GmailKhachHang = $gmailKhachHang;
        $this->TenKhachHang = $tenKhachHang;
        $this->SdtKhachHang = $sdtKhachHang;
        $this->TinhDi = $tinhDi;
        $this->TinhDen = $tinhDen;
        $this->NgayDi = $ngayDi;
        $this->GiaVe = $giaVe;
        $this->MaMayBay = $maMayBay;
    }
}
$danhSachVeMayBayDaBan=array();
$connect=new mysqli("localhost","root","","quanlymaybay");
if($connect->connect_error){die("that bat". $connect->connect_error);}
$method=$_SERVER['REQUEST_METHOD'];
switch ($method) {
    case 'GET':
        $connect->set_charset('utf8');
        if(isset($_GET['me'])){
            $me=$_GET['me'];
            $gmail=$_GET['gmail'];
            switch ($me) {
                case 'timvemaybaydabantheoma':
                    $query="SELECT * FROM vemaybaydaban WHERE gmailKhachHang='$gmail'";
                    break;
                default:
                    $query="SELECT * FROM vemaybaydaban";
                    break;
            }
        }else{
            $query="SELECT * FROM vemaybaydaban";
        }
        $data=mysqli_query($connect,$query);
        while ($row=mysqli_fetch_array($data)) {
            array_push($danhSachVeMayBayDaBan,
            new VeMayBayDaBan($row['id'],$row['maChuyenBay'],$row['soGhe'],$row['gmailKhachHang'],$row['tenKhachHang'],$row['sdtKhachHang'],$row['tinhDi'],$row['tinhDen'],$row['ngayDi'],$row['giaVe'],$row['mayBay'],)
            );
        }
        echo json_encode($danhSachVeMayBayDaBan);
        break;
    case'POST':{
        $id=$_POST['id'];
        $maChuyenBay=$_POST['maChuyenBay'];
        $soGhe=$_POST['soGhe'];
        $gmailKhachHang=$_POST['gmailKhachHang'];
        $tenKhachHang=$_POST['tenKhachHang'];
        $sdtKhachHang=$_POST['sdtKhachHang'];
        $tinhDi=$_POST['tinhDi'];
        $tinhDen=$_POST['tinhDen'];
        $ngayDi=$_POST['ngayDi'];
        $giaVe=$_POST['giaVe'];
        $maMayBay=$_POST['maMayBay'];
        
        //lay so ghe hiện tại
        $query2="SELECT * FROM chuyenbay WHERE maChuyenBay='$maChuyenBay'";
        $data=mysqli_query($connect,$query2);
        while ($row=mysqli_fetch_array($data)) {
            $soGhe=$row['soGhe'];
        }
        $query1="INSERT INTO vemaybaydaban VALUES (null,'$maChuyenBay','$soGhe','$gmailKhachHang','$tenKhachHang','$sdtKhachHang','$tinhDi','$tinhDen','$ngayDi','$giaVe','$maMayBay')";
        $soGhe--;
        $query="UPDATE chuyenbay SET soGhe='$soGhe'WHERE maChuyenBay='$maChuyenBay'";
        if($connect->query($query1)==TRUE&&$connect->query($query)) {
            echo "ThanhCong";
        }else{
            echo "ThatBai";
        }
        break;
    }
    default:
        # code...
        break;
    
    $connect->close();
}
?>