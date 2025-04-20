package socket.handler;

import dao.GenericDAO;
import dao.PhieuDatDichVu_DAO;
import dto.PhieuDatDichVuDTO;
import model.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class PhieuDatDichVuHandler implements RequestHandler {

        private final PhieuDatDichVu_DAO dao = new PhieuDatDichVu_DAO();

        @Override
        public Response<?> handle(Request<?> request) throws IOException {
            if (request == null || request.getAction() == null) {
                return new Response<>(false, null);
            }

            String action = request.getAction();
            try {
                switch (action) {
                    case "GET_ALL_PHIEU_DAT_DICH_VU":
                        return handleGetAll();

                    case "GET_PHIEU_DAT_DICH_VU_BY_ID":
                        String id = (String) request.getData();
                        if (id == null || id.isEmpty()) {
                            return new Response<>(false, null);
                        }
                        return handleGetById(id);

                    case "CREATE_PHIEU_DAT_DICH_VU":
                        PhieuDatDichVuDTO createDto = (PhieuDatDichVuDTO) request.getData();
                        if (createDto == null || !isValidPhieuDatDichVuDTO(createDto)) {
                            return new Response<>(false, false);
                        }
                        return handleCreate(createDto);

                    case "UPDATE_PHIEU_DAT_DICH_VU":
                        PhieuDatDichVuDTO updateDto = (PhieuDatDichVuDTO) request.getData();
                        if (updateDto == null || !isValidPhieuDatDichVuDTO(updateDto)) {
                            return new Response<>(false, false);
                        }
                        return handleUpdate(updateDto);

                    case "DELETE_PHIEU_DAT_DICH_VU":
                        String deleteId = (String) request.getData();
                        if (deleteId == null || deleteId.isEmpty()) {
                            return new Response<>(false, false);
                        }
                        return handleDelete(deleteId);

                    case "GET_PHIEU_DAT_DICH_VU_BY_MA_KH":
                        String maKH = (String) request.getData();
                        if (maKH == null || maKH.isEmpty()) {
                            return new Response<>(false, null);
                        }
                        return handleGetByMaKH(maKH);

                    case "GET_PHIEU_DAT_DICH_VU_BY_MA_NV":
                        String maNV = (String) request.getData();
                        if (maNV == null || maNV.isEmpty()) {
                            return new Response<>(false, null);
                        }
                        return handleGetByMaNV(maNV);

                    case "GET_PHIEU_DAT_DICH_VU_BY_NGAY_DAT":
                        LocalDateTime ngayDat = (LocalDateTime) request.getData();
                        if (ngayDat == null) {
                            return new Response<>(false, null);
                        }
                        return handleGetByNgayDat(ngayDat);

                    case "GET_PHIEU_DAT_DICH_VU_BY_NGAY_DAT_RANGE":
                        LocalDateTime[] range = (LocalDateTime[]) request.getData();
                        if (range == null || range.length != 2 || range[0] == null || range[1] == null) {
                            return new Response<>(false, null);
                        }
                        return handleGetByNgayDatRange(range);

//                    case "GET_PHIEU_DAT_DICH_VU_BY_MA_PDP":
//                        String maPDP = (String) request.getData();
//                        if (maPDP == null || maPDP.isEmpty()) {
//                            System.out.println("GET_PHIEU_DAT_DICH_VU_BY_MA_PDP failed: Mã PDP không hợp lệ");
//                            return new Response<>(false, "Mã phiếu đặt phòng không hợp lệ" + maPDP);
//                        }
//                        System.out.println("Handling GET_PHIEU_DAT_DICH_VU_BY_MA_PDP for maPDP: " + maPDP);
//                        return handleGetByMaPDP(maPDP);

                    case "GET_ALL_PHONG_DAT_DICHVU":
                        @SuppressWarnings("unchecked")
                        List<String> maPDPList = (List<String>) request.getData();

                        GenericDAO<PhieuDatDichVu> pddvDAO = new GenericDAO<>(PhieuDatDichVu.class);
                        List<PhieuDatDichVu> danhSachPhieuDatDichVu = pddvDAO.findByCondition(pddv ->
                                pddv.getPhieuDatPhong() != null &&
                                        maPDPList.contains(pddv.getPhieuDatPhong().getMaPDP())
                        );

                        List<String[]> resultList = new ArrayList<>();

                        for (PhieuDatDichVu pddv : danhSachPhieuDatDichVu) {
                            String maPDP = pddv.getPhieuDatPhong().getMaPDP();
                            String tenKhachHang = pddv.getKhachHang().getHoTen();

                            Set<Phong> danhSachPhong = pddv.getPhieuDatPhong().getPhongs();
                            Set<DichVu> danhSachDichVu = pddv.getDichVus();

                            for (Phong phong : danhSachPhong) {
                                String maPhong = phong.getMaPhong();
                                for (DichVu dv : danhSachDichVu) {
                                    String tenDV = dv.getTenDV();
                                    resultList.add(new String[]{maPDP, maPhong, tenKhachHang, tenDV});
                                }
                            }

                        String[][] resultArray = resultList.toArray(new String[0][]);
                        return new Response<>(true, resultArray);
                    }


                return new Response<>(false, null);

                default:
                        return new Response<>(false, null);
                }
            } catch (Exception e) {
                return new Response<>(false, null);
            }
        }

        private Response<List<PhieuDatDichVuDTO>> handleGetAll() {
            List<PhieuDatDichVuDTO> dtos = dao.findAllDTO();
            return new Response<>(true, dtos);
        }

        // Giả sử đây là nơi xử lý lệnh GET_ALL_PHONG_DAT_DICHVU
        public Response<String[][]> getAllPhongDatDichVu() {
            List<PhieuDatDichVu> danhSachPhieuDatDichVu = new GenericDAO<>(PhieuDatDichVu.class).findAll(); // hoặc truy vấn có điều kiện

            List<String[]> ketQua = new ArrayList<>();

            for (PhieuDatDichVu pddv : danhSachPhieuDatDichVu) {
                String maPDP = pddv.getPhieuDatPhong().getMaPDP();
                String tenKhachHang = pddv.getKhachHang().getHoTen();

                Set<Phong> phongSet = pddv.getPhieuDatPhong().getPhongs();
                Set<DichVu> dichVuSet = pddv.getDichVus();

                for (Phong phong : phongSet) {
                    for (DichVu dichVu : dichVuSet) {
                        String[] dong = new String[]{
                                maPDP,
                                phong.getMaPhong(),       // cần có getter trong lớp Phong
                                tenKhachHang,
                                dichVu.getTenDV()
                        };
                        ketQua.add(dong);
                    }
                }
            }

            String[][] resultArray = ketQua.toArray(new String[0][]);
            return new Response<>(true, resultArray);
        }


    private Response<PhieuDatDichVuDTO> handleGetById(String id) {
            PhieuDatDichVuDTO dto = dao.read(id);
            if (dto == null) {
                return new Response<>(false, null);
            }
            return new Response<>(true, dto);
        }

        private Response<Boolean> handleCreate(PhieuDatDichVuDTO dto) {
            try {
                boolean success = dao.create(dto);
                return new Response<>(success, success);
            } catch (IllegalArgumentException | IllegalStateException e) {
                return new Response<>(false, false);
            }
        }

        private Response<Boolean> handleUpdate(PhieuDatDichVuDTO dto) {
            try {
                boolean success = dao.update(dto);
                return new Response<>(success, success);
            } catch (IllegalArgumentException | IllegalStateException e) {
                return new Response<>(false, false);
            }
        }

        private Response<Boolean> handleDelete(String id) {
            try {
                boolean success = dao.delete(id);
                return new Response<>(success, success);
            } catch (IllegalArgumentException | IllegalStateException e) {
                return new Response<>(false, false);
            }
        }

        private Response<List<PhieuDatDichVuDTO>> handleGetByMaKH(String maKH) {
            List<PhieuDatDichVuDTO> dtos = dao.findByMaKH(maKH);
            return new Response<>(true, dtos);
        }

        private Response<List<PhieuDatDichVuDTO>> handleGetByMaNV(String maNV) {
            List<PhieuDatDichVuDTO> dtos = dao.findByMaNV(maNV);
            return new Response<>(true, dtos);
        }

        private Response<List<PhieuDatDichVuDTO>> handleGetByNgayDat(LocalDateTime ngayDat) {
            List<PhieuDatDichVuDTO> dtos = dao.findByNgayDat(ngayDat);
            return new Response<>(true, dtos);
        }

        private Response<List<PhieuDatDichVuDTO>> handleGetByNgayDatRange(LocalDateTime[] range) {
            List<PhieuDatDichVuDTO> dtos = dao.findByNgayDatRange(range[0], range[1]);
            return new Response<>(true, dtos);
        }

        private Response<List<PhieuDatDichVuDTO>> handleGetByMaPDP(String maPDP) {
            try {
                List<PhieuDatDichVuDTO> dtos = dao.findByMaPDP(maPDP);
                return new Response<>(true,  dtos);
            } catch (Exception e) {
                e.printStackTrace();
                return new Response<>(false, null);
            }
        }



    // Phương thức kiểm tra tính hợp lệ của PhieuDatDichVuDTO
        private boolean isValidPhieuDatDichVuDTO(PhieuDatDichVuDTO dto) {
            if (dto.getMaPDDV() == null || dto.getMaPDDV().isEmpty()) {
                return false;
            }
            if (dto.getNgayDatDichVu() == null) {
                return false;
            }
            if (dto.getSoLuongDichVu() < 0) {
                return false;
            }
            if (dto.getMaKH() == null || dto.getMaKH().isEmpty()) {
                return false;
            }
            if (dto.getMaNV() == null || dto.getMaNV().isEmpty()) {
                return false;
            }
            // dsMaDV có thể null, nhưng nếu không null thì không được rỗng
            if (dto.getDsMaDV() != null && dto.getDsMaDV().isEmpty()) {
                return false;
            }
            return true;
        }
    }