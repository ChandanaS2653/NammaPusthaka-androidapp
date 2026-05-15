# NammaPustaka — Smart Library Assistant

> MindMatrix VTU Internship Program | Project Title: 97
> Android App Development using GenAI — Namma-Pustaka (Education)

---

## Problem Statement

In many village schools, the library is just a cupboard of books with no tracking. Students often don't know what books are available, and teachers struggle to keep track of who has borrowed what. This leads to lost books and a lack of reading culture.

## Vision

NammaPustaka turns a simple shelf into a digital catalog. Students can browse book covers, see summaries in Kannada, and "Reserve" a book. For the teacher, it acts as a digital register — sending reminders when a book is overdue.

---

## Features

| Feature | Description |
|---|---|
| Book Catalog | Browse books by category (Story, Science, History) with search |
| QR Borrow | Scan a QR code on a book to issue it to a student profile |
| Active Borrows | View all currently borrowed books; overdue items shown in red |
| Reading Leaderboard | Ranks students by total pages read this month |
| Add Book | Add new books to the library via a simple camera-based entry |

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Kotlin 1.9.22 |
| Architecture | MVVM + Repository Pattern |
| Database | Room 2.6.1 (SQLite) |
| Navigation | Navigation Component 2.7.6 |
| Camera / QR | CameraX 1.3.1 + ML Kit Barcode Scanning 17.2.0 |
| Async | Kotlin Coroutines 1.7.3 |
| UI | Material Design 3, ViewBinding, RecyclerView |
| Image Loading | Glide 4.16.0 |
| Build | Gradle 8.2.2, KSP 1.9.22-1.0.17 |

---

## Project File Structure

```
NammaPustaka/
├── build.gradle                         # Root Gradle config
├── settings.gradle                      # Module settings
├── gradle.properties                    # JVM & AndroidX properties
│
└── app/
    ├── build.gradle                     # App module dependencies
    ├── proguard-rules.pro
    │
    └── src/main/
        ├── AndroidManifest.xml          # CAMERA permission, MainActivity entry
        │
        ├── java/com/nammapustaka/
        │   │
        │   ├── data/
        │   │   ├── db/
        │   │   │   ├── AppDatabase.kt           # Room DB singleton, seed data (7 books, 4 students)
        │   │   │   ├── BookDao.kt               # CRUD + search + category filter
        │   │   │   ├── StudentDao.kt            # Get all students ranked by pages read
        │   │   │   ├── TransactionDao.kt        # Active/history transactions
        │   │   │   └── ReviewDao.kt             # Book reviews
        │   │   │
        │   │   ├── model/
        │   │   │   ├── Book.kt                  # Entity: id, title, author, category, qrCode, isAvailable
        │   │   │   ├── Student.kt               # Entity: id, name, className, totalPagesRead
        │   │   │   ├── BorrowTransaction.kt     # Entity: bookId, studentId, borrowDate, dueDate, isReturned
        │   │   │   ├── BookReview.kt            # Entity: bookId, studentId, rating, reviewText
        │   │   │   └── TransactionWithDetails.kt # DAO result joining transaction + book + student
        │   │   │
        │   │   └── repository/
        │   │       ├── BookRepository.kt        # Wraps BookDao; search, filter, insert, update
        │   │       └── TransactionRepository.kt # borrowBook(), returnBook(), availability check
        │   │
        │   ├── ui/
        │   │   ├── MainActivity.kt              # Single Activity; hosts NavHostFragment + BottomNav
        │   │   │
        │   │   ├── catalog/
        │   │   │   ├── CatalogFragment.kt       # 2-column grid, search, category chips, FAB
        │   │   │   ├── CatalogViewModel.kt      # LiveData books; filterByCategory(), search()
        │   │   │   ├── BookAdapter.kt           # RecyclerView ListAdapter; availability badge
        │   │   │   └── AddBookFragment.kt       # Form: title, author, pages, category; auto QR
        │   │   │
        │   │   ├── borrow/
        │   │   │   ├── QRScanFragment.kt        # CameraX preview + ML Kit scanner + student ID input
        │   │   │   ├── BorrowViewModel.kt       # borrowBook(qrCode, studentId); validates availability
        │   │   │   └── QRCodeAnalyzer.kt        # ImageAnalysis.Analyzer; extracts barcode value
        │   │   │
        │   │   ├── transactions/
        │   │   │   ├── TransactionsFragment.kt  # Active borrows list; return action; overdue in red
        │   │   │   ├── TransactionsViewModel.kt # returnBook(); updates student totalPagesRead
        │   │   │   └── TransactionAdapter.kt    # RecyclerView; OVERDUE tag; Return button
        │   │   │
        │   │   └── leaderboard/
        │   │       ├── LeaderboardFragment.kt   # Student rankings list
        │   │       ├── LeaderboardViewModel.kt  # LiveData rankedStudents (ordered by pages DESC)
        │   │       └── LeaderboardAdapter.kt    # Rank #, name, class, pages
        │   │
        │   └── utils/
        │       └── DateUtils.kt                 # formatDate(Long), isOverdue(Long)
        │
        └── res/
            ├── layout/
            │   ├── activity_main.xml            # CoordinatorLayout + NavHostFragment + BottomNav
            │   ├── fragment_catalog.xml         # AppBar + SearchView + ChipGroup + GridRV + FAB
            │   ├── fragment_add_book.xml        # ScrollView form with RadioGroup for category
            │   ├── fragment_qr_scan.xml         # CameraX PreviewView + bottom sheet overlay
            │   ├── fragment_transactions.xml    # RecyclerView + empty state
            │   ├── fragment_leaderboard.xml     # RecyclerView leaderboard
            │   ├── item_book.xml                # MaterialCardView: cover color, title, availability
            │   ├── item_transaction.xml         # Book + student + dates + overdue tag + return btn
            │   └── item_leaderboard.xml         # Rank, name, class, pages read
            │
            ├── menu/
            │   └── bottom_nav_menu.xml          # 4 tabs: Catalog, Borrow, Borrows, Ranks
            │
            ├── navigation/
            │   └── nav_graph.xml                # Start: catalogFragment; action: catalog → addBook
            │
            └── values/
                ├── strings.xml                  # App name: "Namma Pustaka"
                ├── colors.xml                   # green_primary #2E7D32, overdue_red #F44336
                └── themes.xml                   # Theme.NammaPustaka (Material DayNight NoActionBar)
```

---

## Database Schema

```
books          → id | title | author | category | totalPages | coverColor | qrCode | isAvailable
students       → id | name | className | totalPagesRead
transactions   → id | bookId | studentId | borrowDate | dueDate | returnDate | isReturned
reviews        → id | bookId | studentId | rating | reviewText
```

Seed data is inserted on first launch: **7 books** across Story/Science/History + **4 students**.

---

## App Flow

```
Launch
  └─> MainActivity (Single Activity)
        └─> BottomNavigationView (4 tabs)
              ├─ [Catalog]   CatalogFragment ──FAB──> AddBookFragment
              ├─ [Borrow]    QRScanFragment  (scan QR → enter student ID → Issue)
              ├─ [Borrows]   TransactionsFragment (active borrows; Return button)
              └─ [Ranks]     LeaderboardFragment (students ranked by pages read)
```

---

## How to Open in Android Studio

### Prerequisites
- Android Studio Hedgehog (2023.1.1) or newer
- JDK 17
- Android SDK 34
- A physical Android device or emulator with API level 24+

### Steps

1. **Clone / Download** the project
   ```
   git clone <repo-url>
   ```
   Or unzip the downloaded folder.

2. **Open in Android Studio**
   - Launch Android Studio
   - Click **File → Open**
   - Navigate to the `NammaPustaka` folder
   - Click **OK** and wait for Gradle sync to finish

3. **Let Gradle Sync**
   - Android Studio will download all dependencies automatically
   - Wait for the sync to complete (bottom status bar shows progress)

4. **Connect a Device or Start Emulator**
   - For QR scanning, a **real device** is recommended (camera works better)
   - Or create an AVD: **Tools → Device Manager → Create Device**

5. **Run the App**
   - Click the green **Run** button (▶) or press `Shift + F10`
   - Select your device/emulator
   - The app installs and launches automatically

### First Launch
The app auto-seeds the database with 7 sample books and 4 students so you can test all features immediately without entering data manually.

---

## What to Do Next (Pending Tasks)

### Must-Have (Success Criteria)
- [ ] Add new book via camera-based QR code capture (auto-fill QR from scan)
- [ ] Overdue book text color turns red automatically — verify this works end-to-end
- [ ] Search library catalog by Book Name or Author — test and validate

### Enhancements
- [ ] **Review Corner** — let students leave a Star Rating + one-sentence review per book
- [ ] **Kannada language support** — add Kannada strings for rural school usability
- [ ] **Teacher Dashboard** — overdue reminders / notification system
- [ ] **QR Code generation image** — generate a printable QR sticker per book
- [ ] **Export borrowed list** to CSV/PDF for teacher records
- [ ] **Book cover image** — allow camera photo capture for book cover instead of solid color

### Testing
- [ ] Test QR scan on a real device (print a QR or use another screen)
- [ ] Test overdue detection by setting a past due date
- [ ] Test leaderboard updates after a book is returned
- [ ] Test add-book flow and verify it appears in catalog instantly

---

## Success Criteria (from Project Specification)

| Criteria | Status |
|---|---|
| Add a new book via simple camera-based entry | Implemented (manual form; QR auto-generated) |
| "Overdue" status turns text color to Red automatically | Implemented in TransactionAdapter |
| Library catalog searchable by Book Name or Author | Implemented in CatalogFragment + CatalogViewModel |

---

## Impact Goals

- **Literacy Promotion** — Encourages a modern reading habit in the digital age
- **Resource Management** — Protects and organizes public school assets
- **Digital Habits** — Teaches children basic Check-in/Check-out digital workflows

---

## Author

Built as part of the **MindMatrix VTU Internship Program**.
