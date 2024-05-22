<?php
class VeMayBay{
    function __construct($id,$maChuyenBay,$maMayBay,$tenTinhDi,$tenTinhDen,$thoiGianDi,$thoiGianDen,$soGhe,$giaVe,$thongTinThem) {
        $this->Id = $id;
        $this->MaChuyenBay=$maChuyenBay;
        $this->MaMayBay=$maMayBay;
        $this->TenTinhDi=$tenTinhDi;
        $this->TenTinhDen=$tenTinhDen;
        $this->ThoiGianDi=$thoiGianDi;
        $this->ThoiGianDen=$thoiGianDen;
        $this->SoGhe=$soGhe;
        $this->GiaVe=$giaVe;
        $this->ThongTinThem=$thongTinThem;
    }
}

$danhSachVeMayBay=array();
$connect=new mysqli("localhost","root","","quanlymaybay");
if($connect->connect_error) {die("Ket Noi That Bai". $connect->connect_error); }

$method=$_SERVER['REQUEST_METHOD'];
switch($method) {
    case 'GET': {
        $connect->set_charset('utf8');
        if(isset($_GET['me'])){
            $tinhDi = $_GET['tinhDi'];
            $tinhDen = $_GET['tinhDen'];
            $ngayDi = $_GET['ngayDi'];
            $me = $_GET['me'];
            switch($me) {
                case 'timvemaybaygoiy': {
                    $query = "SELECT * FROM chuyenbay WHERE tenTinhDi='$tinhDi' AND tenTinhDen='$tinhDen'";
                    break;
                }
                case 'timvemaybay': {
                    // Xử lý hành động khi 'me' là 'timvemaybay'
                    $query = "SELECT * FROM chuyenbay WHERE tenTinhDi='$tinhDi' AND tenTinhDen='$tinhDen'AND DATE(thoiGianDi)='$ngayDi'";
                    //DATE(thoiGianDi)='$ngayDi' AND
                    break;
                }
                case 'timvemaybaytheoma':{
                    //chu y phan nay
                    $query = "SELECT * FROM chuyenbay WHERE maChuyenBay='$tinhDi'";
                    break;
                }  
                default: {
                    // Xử lý hành động mặc định khi 'me' không phù hợp với bất kỳ trường hợp nào khác
                    $query="SELECT * FROM chuyenbay";
                    break;
                }
            }
        } else {
            // Xử lý trường hợp không có biến 'me' được gửi trong request
            $query="SELECT * FROM chuyenbay";
        }
        $data=mysqli_query($connect,$query);
        while($row=mysqli_fetch_array($data)) {
            array_push($danhSachVeMayBay,
            new VeMayBay($row['id'],$row['maChuyenBay'],$row['maMayBay'],$row['tenTinhDi'],$row['tenTinhDen'],$row['thoiGianDi'],$row['thoiGianDen'],$row['soGhe'],$row['giaVe'],$row['thongTinThem']));
        }
        echo json_encode($danhSachVeMayBay);
        break;
    }
    case 'POST':{

        //lấy dữ liệu từ app gửi về
        $meth=$_POST['ME'];
        $id=$_POST['id'];
        $maChuyenBay=$_POST['maChuyenBay'];
        $maMayBay=$_POST['maMayBay'];
        $tenTinhDi=$_POST['tenTinhDi'];
        $tenTinhDen=$_POST['tenTinhDen'];
        $thoiGianDi=$_POST['thoiGianDi'];
        $thoiGianDen=$_POST['thoiGianDen'];
        $soGhe=$_POST['soGhe'];
        $giaVe=$_POST['giaVe'];
        $thongTinThem=$_POST['thongTinThem'];
        // đọc lại nó đuói dạng an toàn
        {
        $id=$connect->real_escape_string($id);
        $maChuyenBay=$connect->real_escape_string($maChuyenBay);
        $maMayBay=$connect->real_escape_string($maMayBay);
        $tenTinhDi=$connect->real_escape_string($tenTinhDi);
        $tenTinhDen=$connect->real_escape_string($tenTinhDen);
        $thoiGianDi=$connect->real_escape_string($thoiGianDi);
        $thoiGianDen=$connect->real_escape_string($thoiGianDen);
        $soGhe=$connect->real_escape_string($soGhe);
        $giaVe=$connect->real_escape_string($giaVe);
        $thongTinThem=$connect->real_escape_string($thongTinThem);
        }
        switch ($meth) {
            case 'update':
                $query="UPDATE chuyenbay SET maChuyenBay='$maChuyenBay',maMayBay='$maMayBay',tenTinhDi='$tenTinhDi',tenTinhDen='$tenTinhDen',thoiGianDi='$thoiGianDi',thoiGianDen='$thoiGianDen',soGhe='$soGhe',giaVe='$giaVe',thongTinThem='$thongTinThem'WHERE id='$id'";
                if($connect->query($query)==TRUE) {
                    echo "ThanhCong";
                }else{
                    echo "ThatBai";
                }
                break;  
            case 'delete':
                $query="DELETE FROM chuyenbay WHERE id='$id'";
                if($connect->query($query)==TRUE) {
                    echo "ThanhCong";
                }else{
                    echo "ThatBai";
                }
                break;  
            case 'insert':
                $query="INSERT INTO chuyenbay VALUES (null,'$maChuyenBay','$maMayBay','$tenTinhDi','$tenTinhDen','$thoiGianDi','$thoiGianDen','$soGhe','$giaVe','$thongTinThem')";
                if($connect->query($query)==TRUE) {
                    echo "ThanhCong";
                }else{
                    echo "ThatBai";
                }
                break;
            
        }
        break;
    }
    default:
        http_response_code(405);
        echo json_encode(array("message" => "Method not allowed"));
        break;
    }
    $connect->close();
?>