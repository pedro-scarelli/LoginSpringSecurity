ALTER TABLE tb_users
  ADD COLUMN st_otp_code VARCHAR(4);

ALTER TABLE tb_users
  ADD COLUMN dt_otp_timestamp TIMESTAMPTZ;
