import { GET, POST, PATCH, DELETE } from "../core/http.js";

export const AuthAPI = {
  signup({ email, password, passwordCheck, nickname, profileImage }) {
    return POST("/api/v1/users", {
      email,
      password,

      password_check: passwordCheck,
      nickname,

      profile_image: profileImage ?? null,

      userRole: null,
    });
  },

  getUser(userId) {
    return GET(`/api/v1/users/${userId}`);
  },

  updateProfile(userId, { nickname, profileImage }) {
    return PATCH(`/api/v1/users/${userId}/profile`, {
      nickname,
      profileImage: profileImage ?? null,
    });
  },

  updatePassword(userId, { oldPassword, newPassword, newPasswordCheck }) {
    return PATCH(`/api/v1/users/${userId}/password`, {
      oldPassword,
      newPassword,
      newPasswordCheck,
    });
  },

  deleteUser(userId) {
    return DELETE(`/api/v1/users/${userId}`);
  },
};
