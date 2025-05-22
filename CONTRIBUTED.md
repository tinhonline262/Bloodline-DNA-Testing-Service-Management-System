# 📖 Quy định làm việc chung dự án Bloodline System

## 📌 Quy trình làm việc

1. Nhận task từ Leader
2. Tạo nhánh mới từ `develop`
3. Code và test local
4. Commit đúng cú pháp
5. Push code lên nhánh feature
6. Tạo Pull Request (PR) vào `develop`
7. Reviewer review & test
8. Merge PR
9. Xóa nhánh sau khi merge

---

## 📌 Quy ước đặt tên nhánh

| Loại | Cấu trúc | Ví dụ |
|:------|:----------------|:---------------------------|
| Tính năng mới | `feature/tinh-nang` | `feature/booking` |
| Sửa bug | `bugfix/ten-bug` | `bugfix/fix-bug-login` |
| Cải thiện | `refactor/ten-chuc-nang` | `refactor/update-api-booking` |

---

## 📌 Quy tắc commit message

| Loại commit | Cú pháp                                    | Ví dụ                                 |
| :---------- | :----------------------------------------- | :------------------------------------ |
| Thêm mới    | `feat: #(số task trong issue) - mô tả`     | `feat: #12 - add feature booking` |
| Sửa bug     | `fix: #(số task trong issue) - mô tả`      | `fix: #21 - fix bug validate email`   |
| Refactor    | `refactor: #(số task trong issue) - mô tả` | `refactor: #18 - refactor API booking`  |
| Cấu hình    | `chore: #(số task trong issue) - mô tả`    | `chore: #5 - update Docker compose`   |


---

