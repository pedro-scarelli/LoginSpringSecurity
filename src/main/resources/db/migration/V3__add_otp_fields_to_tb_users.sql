ALTER TABLE tb_users
  ADD COLUMN it_otp_code INTEGER;

ALTER TABLE tb_users
  ADD COLUMN dt_otp_timestamp TIMESTAMPTZ;
