- **auth.users** → AUTH (email, password)
- **profiles** → User (data aplikasi)
- **user_bmi** → BMI user
- **exercises** → Master latihan
- **history_exercise** → Riwayat latihan user

```sql
auth.users
   ↓
profiles (User)
   ├── user_bmi (0..1)
   └── history_exercise (0..*)
           └── exercises (1)
```

# Buat DataType Enum
```sql
create type gender as enum ('MALE', 'FEMALE');

create type category_bmi as enum (
  'UNDERWEIGHT',
  'NORMAL',
  'OVERWEIGHT',
  'OBESE'
);
```

# Tabel User

## Tabel `profiles`

```sql
create table profiles (
  id uuid primary key references auth.users(id) on delete cascade,
  full_name text,
  email text,
  created_at timestamp default now()
);
```

## Handle Trigger User
*Supaya setiap register otomatis masuk `profiles`.*

```sql
create function public.handle_new_user()
returns trigger as $$
begin
  insert into public.profiles (id, email)
  values (new.id, new.email);
  return new;
end;
$$ language plpgsql security definer;

create trigger on_auth_user_created
after insert on auth.users
for each row execute procedure public.handle_new_user();
```

# Table `user_bmi`
*Relasi: 1 user → 0..1 BMI*

```sql
create table user_bmi (
  id serial primary key,
  user_id uuid unique references profiles(id) on delete cascade,
  gender gender,
  height int,
  weight int,
  age int,
  bmi_value float,
  cateogry_value category_bmi,
  created_at timestamp default now()
  );
```

# Table `Exercises (Master)`

```sql
create table exercises (
  id smallint primary key,
  code text unique not null,
  name text not null,
  met float not null,
  description text
);
```
 
# Table history_exercise
* Relasi: *
* User → 0..* *
* Exercise → 1 * 
* PostgreSQL, INTERVAL adalah tipe data khusus untuk menyimpan DURASI WAKTU
```sql
Create table history_exercise (
  id serial primary key, 
  user_id uuid references profiles(id) on delete cascade, 
  exercise_id smallint references exercises(id), 
  reps int, 
  duration interval, 
  total_calorie int, 
  created_at timestamp default now() 
); 	
```
