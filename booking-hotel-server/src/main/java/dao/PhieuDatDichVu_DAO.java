package dao;

import model.HoaDon;
import model.KhachHang;
import model.NhanVien;
import model.PhieuDatDichVu;
import dto.PhieuDatDichVuDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import utils.HibernateUtil;
import mapper.impl.PhieuDatDichVuMapperImpl;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class PhieuDatDichVu_DAO extends GenericDAO<PhieuDatDichVu> {

    private static final Logger logger = Logger.getLogger(PhieuDatDichVu_DAO.class.getName());
    private final PhieuDatDichVuMapperImpl mapper = new PhieuDatDichVuMapperImpl();

    public PhieuDatDichVu_DAO() {
        super(PhieuDatDichVu.class);
    }

    // Create: Thêm một phiếu đặt dịch vụ mới
    public boolean create(PhieuDatDichVuDTO dto) {
        if (dto == null || dto.getMaPDDV() == null || dto.getMaPDDV().isEmpty()) {
            logger.severe("Mã phiếu đặt dịch vụ không được null hoặc rỗng");
            throw new IllegalArgumentException("Mã phiếu đặt dịch vụ không được null hoặc rỗng");
        }

        if (exists(dto.getMaPDDV())) {
            logger.warning("Phiếu đặt dịch vụ với mã " + dto.getMaPDDV() + " đã tồn tại");
            throw new IllegalStateException("Phiếu đặt dịch vụ với mã " + dto.getMaPDDV() + " đã tồn tại");
        }

        PhieuDatDichVu entity = mapper.toEntity(dto);
        EntityManager em = HibernateUtil.getEntityManager();
        EntityTransaction tr = null;
        try {
            tr = em.getTransaction();
            tr.begin();
            em.persist(entity);
            tr.commit();
            logger.info("Thêm phiếu đặt dịch vụ thành công: " + entity.getMaPDDV());
            return true;
        } catch (Exception ex) {
            logger.severe("Lỗi khi thêm phiếu đặt dịch vụ: " + ex.getMessage());
            if (tr != null && tr.isActive()) {
                tr.rollback();
            }
            return false;
        } finally {
            em.close();
        }
    }

    // Read: Lấy một phiếu đặt dịch vụ theo mã
    public PhieuDatDichVuDTO read(String id) {
        if (id == null || id.isEmpty()) {
            logger.severe("Mã phiếu đặt dịch vụ phải là một chuỗi không null");
            throw new IllegalArgumentException("Mã phiếu đặt dịch vụ phải là một chuỗi không null");
        }

        EntityManager em = HibernateUtil.getEntityManager();
        try {
            PhieuDatDichVu entity = em.find(PhieuDatDichVu.class, id);
            if (entity != null) {
                logger.info("Lấy phiếu đặt dịch vụ thành công: " + id);
                return mapper.toDTO(entity);
            } else {
                logger.warning("Không tìm thấy phiếu đặt dịch vụ với mã: " + id);
                return null;
            }
        } finally {
            em.close();
        }
    }

    // Update: Cập nhật thông tin phiếu đặt dịch vụ
    public boolean update(PhieuDatDichVuDTO dto) {
        if (dto == null || dto.getMaPDDV() == null || dto.getMaPDDV().isEmpty()) {
            logger.severe("Mã phiếu đặt dịch vụ không được null hoặc rỗng");
            throw new IllegalArgumentException("Mã phiếu đặt dịch vụ không được null hoặc rỗng");
        }

        if (!exists(dto.getMaPDDV())) {
            logger.warning("Phiếu đặt dịch vụ với mã " + dto.getMaPDDV() + " không tồn tại");
            throw new IllegalStateException("Phiếu đặt dịch vụ với mã " + dto.getMaPDDV() + " không tồn tại");
        }

        PhieuDatDichVu entity = mapper.toEntity(dto);
        EntityManager em = HibernateUtil.getEntityManager();
        EntityTransaction tr = null;
        try {
            tr = em.getTransaction();
            tr.begin();
            em.merge(entity);
            tr.commit();
            logger.info("Cập nhật phiếu đặt dịch vụ thành công: " + entity.getMaPDDV());
            return true;
        } catch (Exception ex) {
            logger.severe("Lỗi khi cập nhật phiếu đặt dịch vụ: " + ex.getMessage());
            if (tr != null && tr.isActive()) {
                tr.rollback();
            }
            return false;
        } finally {
            em.close();
        }
    }

    // Delete: Xóa một phiếu đặt dịch vụ theo mã
    public boolean delete(String id) {
        if (id == null || id.isEmpty()) {
            logger.severe("Mã phiếu đặt dịch vụ phải là một chuỗi không null");
            throw new IllegalArgumentException("Mã phiếu đặt dịch vụ phải là một chuỗi không null");
        }

        if (!exists(id)) {
            logger.warning("Phiếu đặt dịch vụ với mã " + id + " không tồn tại");
            throw new IllegalStateException("Phiếu đặt dịch vụ với mã " + id + " không tồn tại");
        }

        EntityManager em = HibernateUtil.getEntityManager();
        EntityTransaction tr = null;
        try {
            tr = em.getTransaction();
            tr.begin();
            PhieuDatDichVu entity = em.find(PhieuDatDichVu.class, id);
            if (entity != null) {
                em.remove(entity);
                tr.commit();
                logger.info("Xóa phiếu đặt dịch vụ thành công: " + id);
                return true;
            }
            return false;
        } catch (Exception ex) {
            logger.severe("Lỗi khi xóa phiếu đặt dịch vụ: " + ex.getMessage());
            if (tr != null && tr.isActive()) {
                tr.rollback();
            }
            return false;
        } finally {
            em.close();
        }
    }

    // Lấy tất cả phiếu đặt dịch vụ
    public List<PhieuDatDichVuDTO> findAllDTO() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            List<PhieuDatDichVu> entities = super.findAll();
            List<PhieuDatDichVuDTO> dtos = entities.stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
            logger.info("Lấy danh sách phiếu đặt dịch vụ thành công, số lượng: " + dtos.size());
            return dtos;
        } finally {
            em.close();
        }
    }

    // Lấy danh sách phiếu đặt dịch vụ theo mã khách hàng
    public List<PhieuDatDichVuDTO> findByMaKH(String maKH) {
        Predicate<PhieuDatDichVu> predicate = pddv -> {
            KhachHang khachHang = pddv.getKhachHang();
            return khachHang != null && maKH.equals(khachHang.getMaKH());
        };
        List<PhieuDatDichVu> entities = findByCondition(predicate);
        List<PhieuDatDichVuDTO> dtos = entities.stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
        logger.info("Lấy danh sách phiếu đặt dịch vụ theo mã khách hàng " + maKH + ", số lượng: " + dtos.size());
        return dtos;
    }

    // Lấy danh sách phiếu đặt dịch vụ theo mã nhân viên
    public List<PhieuDatDichVuDTO> findByMaNV(String maNV) {
        Predicate<PhieuDatDichVu> predicate = pddv -> {
            NhanVien nhanVien = pddv.getNhanVien();
            return nhanVien != null && maNV.equals(nhanVien.getMaNhanVien());
        };
        List<PhieuDatDichVu> entities = findByCondition(predicate);
        List<PhieuDatDichVuDTO> dtos = entities.stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
        logger.info("Lấy danh sách phiếu đặt dịch vụ theo mã nhân viên " + maNV + ", số lượng: " + dtos.size());
        return dtos;
    }

    // Lấy danh sách phiếu đặt dịch vụ theo ngày đặt
    public List<PhieuDatDichVuDTO> findByNgayDat(LocalDateTime ngayDat) {
        Predicate<PhieuDatDichVu> predicate = pddv -> pddv.getNgayDatDichVu().equals(ngayDat);
        List<PhieuDatDichVu> entities = findByCondition(predicate);
        List<PhieuDatDichVuDTO> dtos = entities.stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
        logger.info("Lấy danh sách phiếu đặt dịch vụ theo ngày đặt " + ngayDat + ", số lượng: " + dtos.size());
        return dtos;
    }

    // Lấy danh sách phiếu đặt dịch vụ theo khoảng thời gian
    public List<PhieuDatDichVuDTO> findByNgayDatRange(LocalDateTime start, LocalDateTime end) {
        Predicate<PhieuDatDichVu> predicate = pddv ->
                !pddv.getNgayDatDichVu().isBefore(start) && !pddv.getNgayDatDichVu().isAfter(end);
        List<PhieuDatDichVu> entities = findByCondition(predicate);
        List<PhieuDatDichVuDTO> dtos = entities.stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
        logger.info("Lấy danh sách phiếu đặt dịch vụ theo khoảng thời gian từ " + start + " đến " + end + ", số lượng: " + dtos.size());
        return dtos;
    }

    // Lấy danh sách phiếu đặt dịch vụ theo mã hóa đơn
    public List<PhieuDatDichVuDTO> findByMaHD(String maHD) {
        Predicate<PhieuDatDichVu> predicate = pddv -> {
            HoaDon hoaDon = pddv.getHoaDon();
            return hoaDon != null && maHD.equals(hoaDon.getMaHD());
        };
        List<PhieuDatDichVu> entities = findByCondition(predicate);
        List<PhieuDatDichVuDTO> dtos = entities.stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
        logger.info("Lấy danh sách phiếu đặt dịch vụ theo mã hóa đơn " + maHD + ", số lượng: " + dtos.size());
        return dtos;
    }

    // Lấy danh sách phiếu đặt dịch vụ theo mã phiếu đặt phòng
    public List<PhieuDatDichVuDTO> findByMaPDP(String maPDP) {
        Predicate<PhieuDatDichVu> predicate = pddv ->
                pddv.getPhieuDatPhong() != null && maPDP.equals(pddv.getPhieuDatPhong().getMaPDP());

        List<PhieuDatDichVu> entities = findByCondition(predicate);
        List<PhieuDatDichVuDTO> dtos = entities.stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());

        logger.info("Lấy danh sách phiếu đặt dịch vụ theo mã phiếu đặt phòng " + maPDP + ", số lượng: " + dtos.size());
        return dtos;
    }

    // Kiểm tra xem một phiếu đặt dịch vụ có tồn tại không
    public boolean exists(String maPDDV) {
        PhieuDatDichVuDTO result = read(maPDDV);
        return result != null;
    }
}