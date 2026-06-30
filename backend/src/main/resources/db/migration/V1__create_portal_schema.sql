CREATE TABLE IF NOT EXISTS users (
  id BIGSERIAL PRIMARY KEY,
  first_name VARCHAR(255) NOT NULL,
  last_name VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  date_of_birth DATE,
  role VARCHAR(32) NOT NULL DEFAULT 'PATIENT'
);

ALTER TABLE users ADD COLUMN IF NOT EXISTS role VARCHAR(32) NOT NULL DEFAULT 'PATIENT';

CREATE TABLE IF NOT EXISTS appointment (
  id BIGSERIAL PRIMARY KEY,
  patient_id BIGINT NOT NULL REFERENCES users(id),
  provider VARCHAR(255) NOT NULL,
  specialty VARCHAR(255) NOT NULL,
  scheduled_at TIMESTAMP NOT NULL,
  status VARCHAR(32) NOT NULL DEFAULT 'CONFIRMED',
  reason VARCHAR(1000)
);

CREATE TABLE IF NOT EXISTS medical_record (
  id BIGSERIAL PRIMARY KEY,
  patient_id BIGINT NOT NULL REFERENCES users(id),
  recorded_on DATE NOT NULL,
  title VARCHAR(255) NOT NULL,
  summary VARCHAR(2000)
);

CREATE INDEX IF NOT EXISTS idx_appointment_patient ON appointment(patient_id);
CREATE INDEX IF NOT EXISTS idx_appointment_provider ON appointment(provider);
CREATE INDEX IF NOT EXISTS idx_medical_record_patient ON medical_record(patient_id);
