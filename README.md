# Zoo Tour
獲取台北市立動物園資料
這是一個使用 MVVM 架構 並結合 Clean Architecture 分層設計 的 Android 範例專案。

---

## 💾 資料來源

1. 臺北市立動物園 館區簡介
   https://data.taipei/api/v1/dataset/5a0e5fbb-72f8-41c6-908e-2fb25eff9b8a?scope=resourceAquire
2. 臺北市立動物園 動物資料
   https://data.taipei/api/v1/dataset/a3e2b221-75e0-45c1-8f97-75acbd43d613?scope=resourceAquire
3. 臺北市立動物園 植物資料
   https://data.taipei/api/v1/dataset/e20706d8-bf89-4e6a-9768-db2a10bb2ba4?scope=resourceAquire

*以上資料為台北市政府OpenData

---

## 🚀 功能說明

- 主頁為顯示展館清單
- 點擊展館項目可以進入展館詳細資訊頁面及顯示展出的動植物清單
- 點擊動植物項目可以進入動植物詳細資訊頁面

---

## 🧱 專案架構

本專案依照 **Clean Architecture** 進行模組分層，並使用 MVVM 模式處理 UI 狀態與事件：

```plaintext
📦 app/
├── data
│   ├── dataSource            # 數據來源（遠程數據源）
│   ├── repositories          # 資料庫操作邏輯
│   └── entities              # 資料實體（Entity）定義
│
├── viewModel           
│   ├── AreaListViewModel     # 展館清單邏輯處理
│   ├── ExhibitDataViewModel  # 動植物清單邏輯處理
│   └── ZooViewModelFactory   # 自訂 ViewModel 工廠，負責建立並注入 ZooRepository 至各 ViewModel
│
├── view
│   ├── area                  # 展館資訊
│   ├── exhibit               # 展館詳細資料
│   ├── information           # 動植物詳細資料
│   └── webView               # 開啟網頁頁面
│                             
├── ui.theme                  # 自定義主題與樣式
├── util                      # 公用工具類
│                             
├── MainActivity              # APP 進入點，設定 Navigation
└── MainApplication           # Application 類
```
---

## 🐝 單元測試

- AreaRepositoryTest：  
  驗證取得館區資料時，會依序發出 Loading、Success 或 Error 狀態，並檢查資料正確性與錯誤處理。
- AnimalRepositoryTest：  
  驗證取得動物資料時，會依序發出 Loading、Success 或 Error 狀態，並檢查資料正確性與錯誤處理。
- PlantRepositoryTest：  
  驗證取得植物資料時，會依序發出 Loading、Success 或 Error 狀態，並檢查資料正確性與錯誤處理。

測試內容包含：
- 正常回傳資料時，資料內容正確
- 回傳空資料時，能正確處理
- 發生例外時，能正確回傳錯誤狀態與訊息

---

## 🛠️ 使用技術與套件

- **Jetpack Compose** - 建立聲明式 UI
- **retrofit** - 網路請求框架
- **okhttp** - 網路請求框架
- **navigation** - 頁面導航
- **Gson** - JSON 處理（如有假資料序列化/反序列化需求）

---

## 📷 畫面展示

[demo.mp4](demo/demo.mp4)

---

## 👨‍💻 作者
- This project was created by [Hsieh Jia Shiuan]