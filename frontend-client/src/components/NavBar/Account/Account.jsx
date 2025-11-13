import { Link } from "react-router-dom";
import "./Account.css";
import { useAuth, useLogout } from "@/hooks";
import { openLoginModal } from "@/components/Modals/LoginModal";
import { openSignUpModal } from "@/components/Modals/SignUpModal";
import { notifications } from "@mantine/notifications";

const Account = () => {
  const { isAuthenticated, userId } = useAuth();
  const { mutate: logout } = useLogout();

  const handleLogout = () => {
    logout({ variables: null }, { onSuccess: () => {
      notifications.show({ title: 'Adiós!', message: 'Cerraste sesión' });
    } })
  }

  return (
    <div className="account">
      <div className="cart">
        {isAuthenticated && (
          <span className="account-user">{userId || 'User'}</span>
        )}
      </div>
      <div className="login">
        {isAuthenticated ? (
          <button className="btn-rounded small-rounded" onClick={handleLogout}>
            Logout
          </button>
        ) : (
          <>
            <button
              className="btn-rounded small-rounded"
              onClick={openLoginModal}
            >
              Login
            </button>
            <button
              className="btn-rounded small-rounded"
              onClick={openSignUpModal}
              style={{ marginLeft: 8 }}
            >
              Sign Up
            </button>
          </>
        )}
      </div>
    </div>
  );
};

export default Account;
