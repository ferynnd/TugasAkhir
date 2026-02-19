# RULE TREE – SQUAT STANDARD
SquatEvaluation
│
├── 1. Validasi Posisi Awal (START / Standing)
│     │
│     ├── IF Knee Angle 160°–180° ?
│     │      ├── NO → Output: "Lutut belum lurus"
│     │      └── YES
│     │
│     ├── IF Hip Angle 160°–180° ?
│     │      ├── NO → Output: "Pinggul belum ekstensi penuh"
│     │      └── YES
│     │
│     └── IF Torso Angle 80°–100° ?
│            ├── NO → Output: "Postur berdiri tidak netral"
│            └── YES → State = READY
│
├── 2. Fase Turun (DESCENDING)
│     │
│     ├── IF Knee Angle < 150° ?
│     │      └── State → DESCENDING
│     │
│     ├── IF Knee Angle 80°–100° ?
│     │      ├── YES → Depth Achieved
│     │      └── NO → Output: "Belum mencapai kedalaman ideal"
│     │
│     ├── IF Torso Angle 60°–70° ?
│     │      ├── YES → Postur torso benar
│     │      └── NO → Warning:
│     │            ├── <60° → "Terlalu condong ke depan"
│     │            └── >75° → "Kurang condong"
│     │
│     └── IF Hip Angle ±90° ?
│            ├── YES → Posisi bawah valid
│            └── NO → Output: "Pinggul belum sejajar lutut"
│
├── 3. Fase Bawah (BOTTOM)
│     │
│     ├── IF Knee 80°–100°
│     ├── IF Hip 80°–100°
│     ├── IF Torso 60°–70°
│     │
│     └── Semua TRUE → VALID_BOTTOM
│
├── 4. Fase Naik (ASCENDING)
│     │
│     ├── IF Knee Angle > 110° ?
│     │      └── State → ASCENDING
│     │
│     ├── IF Knee Angle 160°–180° ?
│     │      └── Kembali ke START
│     │
│     └── IF Hip tidak kembali ekstensi penuh ?
│            └── Output: "Tidak berdiri sempurna"
│
└── 5. Validasi Repetisi
      │
      ├── IF Sequence:
      │      START → DESC → BOTTOM → ASC → START
      │
      │      ├── YES → reps++
      │      └── NO → Tidak dihitung
      │
      └── IF Bottom tidak valid ?
             └── Output: "Squat tidak mencapai 90°"


1️⃣ Rule Postur Tubuh (Posture Validation)

| No | Kondisi (IF)             | Ambang             | Aksi (THEN)  | Output                           |
| -- | ------------------------ | ------------------ | ------------ | -------------------------------- |
| R1 | Sudut Torso < 55°        | Shoulder–Hip–Knee  | Koreksi      | "Badan terlalu condong ke depan" |
| R2 | Sudut Torso > 75°        | Shoulder–Hip–Knee  | Koreksi      | "Badan terlalu tegak"            |
| R3 | Sudut Torso 60°–70°      | Shoulder–Hip–Knee  | Postur valid | -                                |
| R4 | Lutut masuk ke dalam     | Hip–Knee–Ankle     | Koreksi      | "Lutut tidak sejajar kaki"       |
| R5 | Lutut terlalu melebar    | Hip–Knee–Ankle     | Koreksi      | "Lutut terlalu keluar"           |
| R6 | Lutut sejajar ujung kaki | Hip–Knee–Ankle     | Posisi ideal | -                                |
| R7 | Punggung melengkung      | Spine Alignment    | Koreksi      | "Jaga punggung tetap netral"     |
| R8 | Spine netral (lurus)     | Shoulder–Hip–Ankle | Postur valid | -                                |

2️⃣ Rule Kedalaman Gerakan (Range of Motion)

| No | Kondisi (IF)             | Ambang             | Aksi (THEN)  | Output                           |
| -- | ------------------------ | ------------------ | ------------ | -------------------------------- |
| R1 | Sudut Torso < 55°        | Shoulder–Hip–Knee  | Koreksi      | "Badan terlalu condong ke depan" |
| R2 | Sudut Torso > 75°        | Shoulder–Hip–Knee  | Koreksi      | "Badan terlalu tegak"            |
| R3 | Sudut Torso 60°–70°      | Shoulder–Hip–Knee  | Postur valid | -                                |
| R4 | Lutut masuk ke dalam     | Hip–Knee–Ankle     | Koreksi      | "Lutut tidak sejajar kaki"       |
| R5 | Lutut terlalu melebar    | Hip–Knee–Ankle     | Koreksi      | "Lutut terlalu keluar"           |
| R6 | Lutut sejajar ujung kaki | Hip–Knee–Ankle     | Posisi ideal | -                                |
| R7 | Punggung melengkung      | Spine Alignment    | Koreksi      | "Jaga punggung tetap netral"     |
| R8 | Spine netral (lurus)     | Shoulder–Hip–Ankle | Postur valid | -                                |

3️⃣ Rule Finite State Machine (Repetition Logic)

| No  | State Saat Ini | Kondisi (IF)   | State Berikutnya (THEN) | Keterangan          |
| --- | -------------- | -------------- | ----------------------- | ------------------- |
| R14 | STANDING       | Lutut < 150°   | DESCENDING              | Mulai turun         |
| R15 | DESCENDING     | Lutut 80°–100° | BOTTOM                  | Fase bawah tercapai |
| R16 | BOTTOM         | Lutut > 110°   | ASCENDING               | Mulai naik          |
| R17 | ASCENDING      | Lutut ≥ 165°   | STANDING + reps++       | 1 repetisi valid    |

4️⃣ Rule Validasi Repetisi

| No  | Kondisi (IF)                                              | THEN                                |
| --- | --------------------------------------------------------- | ----------------------------------- |
| R18 | Siklus lengkap: STANDING → DESC → BOTTOM → ASC → STANDING | Tambah 1 repetisi                   |
| R19 | Tidak mencapai BOTTOM (80°–100°)                          | Repetisi tidak dihitung             |
| R20 | Postur tidak valid selama fase BOTTOM                     | Repetisi ditandai tidak sempurna    |
| R21 | Spine tidak netral selama gerakan                         | Repetisi diberi peringatan kualitas |

