package company.danhy.clothesuit.activity.activity.model;

public class Loaisp {
    public int id;
    public String tenSanpham;
    public int giaSanPham;
    public String moTaSanPham;
    public String hinhAnhloaisanpham;
    public int idLoaiSanPham;


    public Loaisp(int id, String tenSanpham, int giaSanPham, String hinhAnhloaisanpham, String moTaSanPham, int idLoaiSanPham) {
        this.id = id;
        this.tenSanpham = tenSanpham;
        this.giaSanPham = giaSanPham;
        this.moTaSanPham = moTaSanPham;
        this.hinhAnhloaisanpham = hinhAnhloaisanpham;
        this.idLoaiSanPham = idLoaiSanPham;
    }

    @Override
    public String toString() {
        return "Sanpham{" +
                "ID=" + id +
                ", Tensanpham='" + tenSanpham + '\'' +
                ", Giasanpham=" + giaSanPham +
                ", Hinhanhsanpham='" + hinhAnhloaisanpham + '\'' +
                ", Motasanpham='" + moTaSanPham + '\'' +
                ", IDSanpham=" + idLoaiSanPham +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTenSanpham() {
        return tenSanpham;
    }

    public void setTenSanpham(String tenSanpham) {
        this.tenSanpham = tenSanpham;
    }

    public int getGiaSanPham() {
        return giaSanPham;
    }

    public void setGiaSanPham(int giaSanPham) {
        this.giaSanPham = giaSanPham;
    }

    public String getMoTaSanPham() {
        return moTaSanPham;
    }

    public void setMoTaSanPham(String moTaSanPham) {
        this.moTaSanPham = moTaSanPham;
    }

    public String getHinhAnhloaisanpham() {
        return hinhAnhloaisanpham;
    }

    public void setHinhAnhloaisanpham(String hinhAnhloaisanpham) {
        this.hinhAnhloaisanpham = hinhAnhloaisanpham;
    }

    public int getIdLoaiSanPham() {
        return idLoaiSanPham;
    }

    public void setIdLoaiSanPham(int idLoaiSanPham) {
        this.idLoaiSanPham = idLoaiSanPham;
    }
}
