package utils.session;
import model.NhanVien;

public class SessionManager {
    private static NhanVien currentUser;

    public static NhanVien getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(NhanVien currentUser) {
        SessionManager.currentUser = currentUser;
    }
    public static boolean isUserLoggedIn() {
        return getCurrentUser() != null;
    }
}

