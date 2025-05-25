# Phân tích hệ thống
## Menu của Admin: 
1. 📊 Dashboard
2. 👥 Quản lý Người dùng
3. 🧬 Quản lý Dịch vụ
4. 📅 Quản lý Đặt hẹn
5. 🔬 Quản lý Xét nghiệm
6. 📋 Quản lý Kết quả
7. 💬 Quản lý Feedback
8. 📝 Quản lý Nội dung
9. 📈 Báo cáo & Thống kê
### 📊 Dashboard
- Một số KPI Cards
  ![image](https://github.com/user-attachments/assets/2c6f9a9b-8cba-42b8-b27a-1228a4e7b8f5)

- Biểu đồ doanh thu theo: ngày/tuàn/tháng/năm
  ![image](https://github.com/user-attachments/assets/f3fae80d-c0e5-43a1-80a9-50e2e6855474)

- Thống kê loại xét nghiệm: Pie chart các loại ADN test
- Tạo đơn hàng mới
- Thêm khách hàng
- Phê duyệt kết quả
- Một số quick action đến Quản lý Kết quả và Quản lý Feedback

### 👥 Quản lý Người dùng

- **Danh sách người dùng**
  - **Tìm kiếm và lọc:** Theo tên, email, số điện thoại, vai trò
  - **Thông tin cơ bản:** Tên, email, SĐT, ngày đăng ký
  - **Trạng thái:** Active / Inactive / Banned / Pending
  - **Hoạt động cuối:** Lần đăng nhập gần nhất

- **CRUD Operations**
  - **Tạo tài khoản:** Cho Staff, Manager, Customer
  - **Chỉnh sửa thông tin:** Cập nhật profile, thông tin liên hệ
  - **Khóa/Mở khóa:** Suspend tài khoản vi phạm
  - **Xóa tài khoản:** Soft delete kèm lý do
  - **Reset mật khẩu:** Gửi link reset qua email

- **Quản lý vai trò**
  - **Danh sách vai trò:**
  Guest
  Customer
  Staff
  Manager
  Admin
  - **Phân quyền chi tiết:**  
  Phân quyền theo từng **module chức năng** (Module-based permissions)
  - **Tạo vai trò mới:**  
  Cho phép tạo **Custom role** với quyền hạn cụ thể theo nhu cầu
  - **Gán vai trò:**  
  Gán role cho người dùng trong hệ thống và cập nhật vai trò khi cần


#### 📊 Ma trận quyền hạn các vai trò

| Module     | Guest | Customer | Staff | Manager | Admin |
|------------|:------|:----------|:-------|:---------|:--------|
| Xem Dịch vụ | ✅    | ✅        | ✅     | ✅       | ✅      |
| Đặt hẹn     | ❌    | ✅        | ✅     | ✅       | ✅      |
| Xét nghiệm  | ❌    | ❌        | ✅     | ✅       | ✅      |
| Kết quả     | ❌    | ✅        | ✅     | ✅       | ✅      |
| Feedback    | ❌    | ✅        | ✅     | ✅       | ✅      |
| Quản lý người dùng | ❌ | ❌    | ❌     | ✅       | ✅      |
| Quản lý vai trò    | ❌ | ❌    | ❌     | ✅       | ✅      |
| Nội dung (Blog, Hướng dẫn) | ✅ | ✅   | ✅     | ✅       | ✅      |
| Dashboard & Báo cáo | ❌ | ❌    | ✅     | ✅       | ✅      |
| Cấu hình hệ thống | ❌ | ❌      | ❌     | ❌       | ✅      |

---

**Chú thích:**  
✅ : Có quyền  
❌ : Không có quyền  




### 🧬 Quản lý Dịch vụ

#### 📌 Loại Xét nghiệm ADN

##### Xét nghiệm Dân sự
- **Xác định huyết thống cha con:** *Paternity test*
- **Xác định huyết thống anh chị em:** *Sibling test*
- **Xác định huyết thống ông bà - cháu:** *Grandparentage*
- **Xét nghiệm ADN mitochondrial:** *Maternal lineage*
- **Xét nghiệm Y-chromosome:** *Paternal lineage*

##### Xét nghiệm Hành chính
- **Xét nghiệm pháp y:** *Forensic DNA analysis*
- **Xác định danh tính:** *Identity verification*
- **Xét nghiệm di truyền:** *Genetic disorders*
- **Xét nghiệm ADN trong tòa án:** *Court-ordered tests*

---

#### 📌 Quản lý Thông tin Dịch vụ

##### Thông tin cơ bản
- **Tên dịch vụ:** Tiếng Việt và Tiếng Anh
- **Mô tả chi tiết:** Thông tin kỹ thuật và yêu cầu
- **Thời gian thực hiện:** 3-7 ngày làm việc
- **Độ chính xác:** 99.99%
- **Yêu cầu mẫu:** Loại mẫu cần thiết cho từng dịch vụ

##### Cấu hình Quy trình
- **Phương thức thu mẫu:** Tại nhà / Tại cơ sở
- **Phê duyệt:** Cần Manager approval hay không

---

#### 📌 Quản lý Giá cả

##### Bảng giá Dịch vụ
- **Giá cơ bản:** Base price cho từng loại dịch vụ
- **Giá theo số lượng mẫu:** Pricing tiers
- **Phí phụ thu:** Rush service, weekend service
- **Giá combo:** Package deals

##### Khuyến mãi và Giảm giá (có thể có hoặc không)
- **Mã giảm giá:** Coupon codes
- **Chương trình khuyến mãi:** Promotional campaigns
- **Giảm giá theo số lượng:** Bulk discounts

---

#### Quản lý Kit Xét nghiệm

- **Thông tin kit:** Danh sách nội dung kit và hướng dẫn sử dụng
- **Kho kit:** Inventory management
- **Tracking kit:** Theo dõi quá trình gửi/nhận kit
- **Expiry date:** Quản lý hạn sử dụng của từng kit

#### Quản lý thanh toán
- **Trạng thái thanh toán:** Paid/UnPaid/Partial
- **Phương thức thanh toán:** Cash/Card/Transfer
- **Refund:** Hoàn tiền

### 📅 Quản lý Đặt hẹn

#### 📌 Danh sách Đặt hẹn

##### Thông tin Đơn hàng
- **Mã đơn hàng:** Unique booking ID
- **Thông tin khách hàng:** Tên, SĐT, địa chỉ
- **Loại xét nghiệm:** DNA test type
- **Ngày đặt / hẹn:** Booking date và Appointment date
- **Trạng thái:** Pending, Confirmed, In Progress, Completed
- **Giá trị đơn hàng:** Tổng số tiền

##### Bộ lọc và Tìm kiếm
- **Theo trạng thái:** Filter by status
- **Theo ngày:** Date range picker
- **Theo loại xét nghiệm:** Test type filter
- **Theo nhân viên:** Assigned staff
- **Theo khách hàng:** Customer search
  
#### 📌 Quản lý Lịch làm việc

##### Cấu hình Lịch
- **Giờ làm việc:** Working hours setup
- **Ngày nghỉ:** Holiday calendar
- **Shift scheduling:** Phân ca làm việc cho staff
- **Capacity management:** Quản lý số lượng appointment/ngày

##### Phân công Nhân viên
- **Auto assignment:** Tự động phân công đơn hàng
- **Manual assignment:** Chỉ định thủ công nhân viên xử lý

##### Thao tác Quản lý
- **Xác nhận đơn hàng:** Approve booking
- **Hủy đơn hàng:** Cancel với lý do cụ thể
- **Thay đổi lịch hẹn:** Reschedule
- **Cập nhật trạng thái:** Status update
- **Gửi thông báo:** Notifications đến khách và nhân viên liên quan



### 🔬 Quản lý Xét nghiệm

#### 📦 Thông tin Mẫu Xét Nghiệm

- **Mã mẫu:** Unique Sample ID
- **Thông tin đơn hàng:** Liên kết với booking
- **Loại mẫu:** Nước bọt, Tóc, Máu, v.v.
- **Ngày thu:** Collection date
- **Nhân viên thu:** Collector information
- **Chất lượng mẫu:** Tốt / Lỗi / Kiểm tra

---

#### 📑 Workflow Phòng Lab trong Quy trình Xét nghiệm

##### 1️⃣ Nhận mẫu
- Mẫu từ khách / shipper / nhân viên thu mẫu được chuyển về phòng lab.
- Dữ liệu tiếp nhận:
  | Trường dữ liệu       | Ý nghĩa                        |
  |:--------------------|:-------------------------------|
  | Mã đơn hàng (Booking ID) | Liên kết mẫu với đơn xét nghiệm |
  | Mã mẫu (Sample Code)     | Gán riêng cho từng mẫu         |
  | Thời gian thu mẫu        | Thời điểm lấy mẫu               |
  | Loại xét nghiệm          | Paternity, Sibling, Mitochondrial… |
  | Người thu mẫu (nếu có)   | Nhân viên thực hiện             |
  | Trạng thái mẫu           | Chưa xử lý / Lỗi / Đang xử lý   |

- Chuyển trạng thái đơn hàng: **Đang xét nghiệm**


##### 2️⃣ Kiểm tra Chất lượng Mẫu (Pre-Test QC)
- Lab technician kiểm tra:
  - Đủ lượng DNA
  - Mẫu sạch, không lẫn tạp chất
  - Thời gian bảo quản hợp lệ

- Nếu mẫu lỗi:
  - Cập nhật trạng thái **Mẫu lỗi**
  - Gửi thông báo tới Admin/Manager

- Log dữ liệu:
  | Mã mẫu | Kết quả kiểm tra | Ghi chú | Thời gian |

##### 3️⃣ Tiến hành Xét nghiệm
- Thực hiện giải trình tự / phân tích di truyền
- Máy trả **Raw Data**
- Ví dụ file dữ liệu:
  
| Marker  | Dad   | Child | Mom   |
| :------ | :---- | :---- | :---- |
| D3S1358 | 15 16 | 16 17 | 15 17 |
| vWA     | 17 18 | 17 19 | 17 19 |
| FGA     | 22 23 | 22 24 | 22 24 |
| D8S1179 | 13 14 | 13 13 | 13 13 |
| D21S11  | 28 30 | 30 31 | 30 31 |
| D18S51  | 14 17 | 14 15 | 14 15 |

##### 4️⃣ Phân tích Kết quả
- Nhân viên phân tích:
- So sánh chỉ số ADN giữa các mẫu (VD: Cha-Con)
- Tính **Chỉ số xác suất huyết thống (PI — Paternity Index)**
- Tổng hợp kết quả cuối

- Dữ liệu lưu:
| Mã đơn hàng | Kết luận | Chỉ số PI | Ghi chú | Ngày thực hiện |

##### 5️⃣ Nhập kết quả vào Hệ thống
- Nhập dữ liệu vào form quản lý:
- Mã đơn hàng
- Mã mẫu
- Loại xét nghiệm
- Thông tin mẫu đối chiếu
- Raw data (nếu cần)
- Chỉ số PI
- Kết luận
- Người nhập kết quả
- Thời gian nhập

- Chuyển trạng thái: **Đã có kết quả chờ duyệt**

##### 6️⃣ Phê duyệt Kết quả
- Manager hoặc Admin:
- Kiểm tra Raw Data
- Xem chỉ số tính toán
- Kiểm tra log xét nghiệm
- Duyệt hoặc yêu cầu xét nghiệm lại

- Log lại:
- Người phê duyệt
- Thời gian phê duyệt

##### 7️⃣ Trả Kết quả
- Hệ thống gửi kết quả cho khách:
- Qua email (PDF hoặc link bảo mật)
- Trên tài khoản hệ thống online

- Dữ liệu trả khách:
  
| Trường dữ liệu | Nội dung |
|:---------------|:------------|
| Mã đơn hàng    |  |
| Loại xét nghiệm | |
| Thông tin mẫu   | Ai với ai, thu khi nào |
| Chỉ số PI (%)   | VD: 99.999% |
| Kết luận        | Có / Không có quan hệ huyết thống |
| Ghi chú bổ sung | Nếu có |
| Thời gian thực hiện | |
| Người xét nghiệm | (Lab technician code) |
| Người phê duyệt | (Manager code) |
| Mã kết quả (Result Code) | Để tra cứu nội bộ |

- **Ví dụ mẫu trả khách (dạng PDF):**
![image](https://github.com/user-attachments/assets/cd6df15e-dad9-4c7e-9670-8bdf8e078eb4)

### 📋 Quản lý Kết quả

#### Danh sách Kết quả

##### Thông tin kết quả
- **Mã kết quả:** Result ID
- **Thông tin đơn hàng:** Booking reference
- **Loại xét nghiệm:** Test type
- **Ngày hoàn thành:** Completion date
- **Trạng thái:** Draft / Reviewed / Approved / Delivered
- **Nhân viên phụ trách:** Responsible staff

###### Bộ lọc & Tìm kiếm
- **Theo trạng thái:** Status filter
- **Theo ngày:** Date range picker
- **Theo loại xét nghiệm:** Test type filter
- **Theo độ ưu tiên:** Priority level filter
- **Theo khách hàng:** Customer search

#### Tạo và Chỉnh sửa Kết quả

##### Nhập kết quả
- **Template-based:** Sử dụng template có sẵn
- **Manual entry:** Nhập kết quả thủ công
- **Validation:** Kiểm tra tính hợp lệ và đầy đủ dữ liệu

##### Định dạng báo cáo
- **Standard format:** Định dạng báo cáo chuẩn

✅ **Chức năng hỗ trợ:**
- Tự động sinh file PDF / export kết quả
- Quản lý lịch sử chỉnh sửa và người chỉnh sửa
- Cho phép duyệt kết quả trước khi gửi khách hàng

#### Phê duyệt
- **Auto-approval:** Tự động phê duyệt
- **Manual-approval:** Phê duyệt thủ công
- **Multi-level approval:** Phê duyệt nhiều cấp

### 💬 Quản lý Feedback

#### 📄 Thông tin feedback
- **ID feedback:** Unique identifier
- **Thông tin khách hàng:** Customer info
- **Loại feedback:** 
  - Rating
  - Complaint
  - Suggestion
- **Nội dung:** Feedback content
- **Categories:** bên dưới
- **Cần cải thiện:** bên dưới
- **Đánh giá:** Star rating (1-5)
- **Ngày tạo:** Creation date
- **Đơn hàng cụ thể:** Có hoặc không
- **Trạng thái:** 
  - New
  - Processing
  - Resolved
  - Closed

#### 📊 Categories
- **Service Quality:** Chất lượng dịch vụ
- **Staff Behavior:** Thái độ nhân viên
- **Result Accuracy:** Độ chính xác kết quả
- **Delivery Time:** Thời gian giao kết quả
- **Pricing:** Giá cả
- **Website/App:** Trải nghiệm nền tảng số

#### Improvement Action
- **Process improvement:** Cải tiến quy trình
- **Staff training:** Đào tạo nhân viên
- **Policy changes:** Thay đổi chính sách
- **System updates:** Cập nhật hệ thống


### 📝 Quản lý Nội dung

#### 🏠 Giao diện Website & Static Pages

##### 📌 Trang chủ
- **Hero section:** Banner chính thu hút
- **Services overview:** Tổng quan dịch vụ nổi bật
- **Why choose us:** Lý do chọn chúng tôi (ưu thế, năng lực)
- **Testimonials:** Lời chứng thực từ khách hàng
- **Contact information:** Thông tin liên hệ

##### 📌 Trang dịch vụ
- **Service details:** Chi tiết từng dịch vụ xét nghiệm
- **Pricing tables:** Bảng giá dịch vụ minh bạch
- **Process explanation:** Giải thích quy trình xét nghiệm từng bước
- **FAQ section:** Khu vực các câu hỏi thường gặp

##### 📌 Static pages
- **About us:** Giới thiệu cơ sở y tế và đội ngũ
- **Privacy policy:** Chính sách bảo mật thông tin khách hàng
- **Terms of service:** Điều khoản sử dụng dịch vụ
- **Contact us:** Form và thông tin liên hệ
  
##### 📚 Content Categories
- **DNA Science:** Khoa học ADN
- **Health & Genetics:** Sức khỏe & di truyền
- **Family History:** Lịch sử gia đình
- **Legal Aspects:** Khía cạnh pháp lý của xét nghiệm ADN
- **Case Studies:** Các nghiên cứu và trường hợp thực tế
- **News & Updates:** Tin tức & cập nhật về dịch vụ, công nghệ ADN

##### 📚 User guides

- **How to collect samples:** Hướng dẫn thu mẫu
- **Understanding results:** Hiểu kết quả
- **Booking process:** Quy trình đặt hẹn
- **FAQ:** Câu hỏi thường gặp



### 📈 Báo cáo & Thống kê

#### Doanh thu (Revenue Reports)
- **Daily revenue:** Doanh thu hàng ngày
- **Monthly/Yearly revenue:** Báo cáo doanh thu theo tháng và năm
- **Revenue by service:** Doanh thu theo từng loại dịch vụ
- **Payment methods:** Thống kê doanh thu theo phương thức thanh toán

#### Cost Analysis
- **Operational costs:** Chi phí vận hành chung
- **Lab costs:** Chi phí vận hành phòng lab

#### Customer Analytics
- **New customers:** Số lượng khách hàng mới trong khoảng thời gian tùy chọn

