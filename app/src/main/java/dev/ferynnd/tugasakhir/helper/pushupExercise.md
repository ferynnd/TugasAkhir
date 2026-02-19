# RULE TREE – PUSH UP STANDARD

PushUpEvaluation
│
├── 1. Posture Validation
│   │
│   ├── Spine Alignment (Shoulder–Hip–Ankle)
│   │   ├── angle < 150° → ❌ "Pinggul terlalu turun"
│   │   ├── angle > 190° → ❌ "Pinggul terlalu naik"
│   │   └── 165°–185° → ✅ "Punggung lurus"
│   │
│   ├── Head Position (Ear–Shoulder–Hip)
│   │   ├── angle < 140° → ❌ "Kepala terlalu menunduk"
│   │   ├── angle > 190° → ❌ "Kepala terlalu mendongak"
│   │   └── 150°–180° → ✅ "Posisi kepala netral"
│   │
│   └── Shoulder Abduction (Upper Arm vs Torso)
│       ├── angle > 70° → ❌ "Lengan terlalu melebar"
│       ├── angle < 20° → ❌ "Lengan terlalu rapat"
│       └── 35°–55° → ✅ "Sudut bahu ideal ±45°"
│
├── 2. Movement Depth Validation
│   │
│   ├── Elbow Angle (Average Left & Right)
│   │   ├── angle > 110° saat fase bawah
│   │   │      → ❌ "Turunkan badan lebih dalam"
│   │   │
│   │   ├── 80°–100°
│   │   │      → ✅ "Depth optimal (~90°)"
│   │   │
│   │   └── < 70°
│   │          → ⚠ "Terlalu dalam (stress berlebih)"
│
├── 3. State Machine (Repetition Logic)
│   │
│   ├── State: TOP
│   │     └── elbow < 150° → DESCENDING
│   │
│   ├── State: DESCENDING
│   │     └── elbow 80°–100° → BOTTOM
│   │
│   ├── State: BOTTOM
│   │     └── elbow > 110° → ASCENDING
│   │
│   ├── State: ASCENDING
│   │     └── elbow ≥ 160° → TOP
│   │             └── reps++
│   │
│   └── Repetition Valid Jika:
│         TOP → DESC → BOTTOM → ASC → TOP
│
└── 4. Final Evaluation Output
    │
    ├── Jika semua rule terpenuhi
    │      → ✅ "Gerakan bagus"
    │
    ├── Jika posture salah
    │      → ❌ Tampilkan feedback prioritas tertinggi
    │
    └── Jika depth tidak tercapai
           → ❌ "Range gerakan belum maksimal"

1️⃣ Rule Postur Tubuh (Posture Validation)

| No | Kondisi (IF)          | Ambang             | Aksi (THEN)  | Output                     |
| -- | --------------------- | ------------------ | ------------ | -------------------------- |
| R1 | Sudut Spine < 150°    | Shoulder–Hip–Ankle | Tolak postur | "Pinggul terlalu turun"    |
| R2 | Sudut Spine > 190°    | Shoulder–Hip–Ankle | Tolak postur | "Pinggul terlalu naik"     |
| R3 | Sudut Spine 165°–185° | Shoulder–Hip–Ankle | Postur valid | -                          |
| R4 | Sudut Head < 140°     | Ear–Shoulder–Hip   | Koreksi      | "Kepala terlalu menunduk"  |
| R5 | Sudut Head > 190°     | Ear–Shoulder–Hip   | Koreksi      | "Kepala terlalu mendongak" |
| R6 | Sudut Bahu > 70°      | Upper Arm–Torso    | Koreksi      | "Lengan terlalu melebar"   |
| R7 | Sudut Bahu < 20°      | Upper Arm–Torso    | Koreksi      | "Lengan terlalu rapat"     |
| R8 | Sudut Bahu 35°–55°    | Upper Arm–Torso    | Posisi ideal | -                          |

2️⃣ Rule Kedalaman Gerakan (Range of Motion)

| No  | Kondisi (IF)                      | Ambang        | Aksi (THEN)         | Output                         |
| --- | --------------------------------- | ------------- | ------------------- | ------------------------------ |
| R9  | Sudut Siku > 110° saat fase bawah | Elbow Flexion | Gerakan tidak valid | "Turunkan badan lebih dalam"   |
| R10 | Sudut Siku 80°–100°               | Elbow Flexion | Depth optimal       | -                              |
| R11 | Sudut Siku < 70°                  | Elbow Flexion | Peringatan          | "Terlalu dalam, risiko cedera" |

3️⃣ Rule Finite State Machine (Repetition Logic)

| No  | State Saat Ini | Kondisi (IF)   | State Berikutnya (THEN) | Keterangan          |
| --- | -------------- | -------------- | ----------------------- | ------------------- |
| R12 | TOP            | Elbow < 150°   | DESCENDING              | Mulai turun         |
| R13 | DESCENDING     | Elbow 80°–100° | BOTTOM                  | Fase bawah tercapai |
| R14 | BOTTOM         | Elbow > 110°   | ASCENDING               | Mulai naik          |
| R15 | ASCENDING      | Elbow ≥ 160°   | TOP + reps++            | 1 repetisi valid    |

4️⃣ Rule Validasi Repetisi

| No  | Kondisi (IF)                                    | THEN                             |
| --- | ----------------------------------------------- | -------------------------------- |
| R16 | Siklus lengkap: TOP → DESC → BOTTOM → ASC → TOP | Tambah 1 repetisi                |
| R17 | Tidak mencapai BOTTOM (80°–100°)                | Repetisi tidak dihitung          |
| R18 | Postur tidak valid selama siklus                | Repetisi ditandai tidak sempurna |
