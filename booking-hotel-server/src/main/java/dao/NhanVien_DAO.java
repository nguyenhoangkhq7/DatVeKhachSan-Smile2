package dao;

import dto.NhanVienDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import mapper.GenericMapper;
import mapper.impl.NhanVienMapperImpl;
import model.NhanVien;
import utils.HibernateUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NhanVien_DAO extends GenericDAO<NhanVien> {
    private final GenericMapper<NhanVien, NhanVienDTO> mapper;

    public NhanVien_DAO() {
        super(NhanVien.class);
        this.mapper = new NhanVienMapperImpl();
    }

    //CRUD
    public boolean create(NhanVienDTO nhanVienDTO) {
        if (nhanVienDTO == null || nhanVienDTO.getMaNhanVien() == null || nhanVienDTO.getMaNhanVien().isEmpty()) {
            return false;
        }
        return super.create(mapper.toEntity(nhanVienDTO));
    }

    public boolean delete(String maNhanVien) {
        if (maNhanVien == null || maNhanVien.isEmpty()) return false;
        return super.delete(maNhanVien);
    }

    public boolean update(NhanVienDTO nhanVienDTO) {
        if (nhanVienDTO == null || nhanVienDTO.getMaNhanVien() == null || nhanVienDTO.getMaNhanVien().isEmpty()) {
            return false;
        }
        return super.update(mapper.toEntity(nhanVienDTO));
    }

    //Tìm nhân viên theo mã
    public NhanVienDTO read(String maNhanVien) {
        if (maNhanVien == null || maNhanVien.isEmpty()) return null;
        return mapper.toDTO(super.read(maNhanVien));
    }

    // Lấy tất cả nhân viên
    public List<NhanVienDTO> getAllNhanVienDTOs() {
        return super.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    // Tìm kiếm nhân viên theo tên gần đúng, trả về danh sách DTO
    public List<NhanVienDTO> findByHoTen(String hoTen) {
        if (hoTen == null || hoTen.isEmpty()) return new ArrayList<>();
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<NhanVien> cq = cb.createQuery(NhanVien.class);
            Root<NhanVien> root = cq.from(NhanVien.class);
            cq.select(root).where(cb.like(cb.lower(root.get("hoTen")), "%" + hoTen.toLowerCase() + "%"));
            return em.createQuery(cq).getResultList().stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
        } finally {
            em.close();
        }
    }

    // Tìm kiếm nhân viên theo nhiều tiêu chí (tên, email, số điện thoại)
    public List<NhanVienDTO> findByMultipleCriteria(String hoTen, String email, String sdt) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<NhanVien> cq = cb.createQuery(NhanVien.class);
            Root<NhanVien> root = cq.from(NhanVien.class);
            List<Predicate> predicates = new ArrayList<>();

            // Tìm theo tên (nếu có)
            if (hoTen != null && !hoTen.isEmpty()) {
                predicates.add((Predicate) cb.like(cb.lower(root.get("hoTen")), "%" + hoTen.toLowerCase() + "%"));
            }

            // Tìm theo email (nếu có)
            if (email != null && !email.isEmpty()) {
                predicates.add((Predicate) cb.equal(root.get("email"), email));
            }

            // Tìm theo số điện thoại (nếu có)
            if (sdt != null && !sdt.isEmpty()) {
                predicates.add((Predicate) cb.equal(root.get("SDT"), sdt));
            }

            cq.select(root).where(predicates.toArray(new Predicate[0]));
            return em.createQuery(cq).getResultList().stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
        } finally {
            em.close();
        }
    }
}