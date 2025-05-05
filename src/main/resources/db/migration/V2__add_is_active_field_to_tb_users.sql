ALTER TABLE tb_users
  ADD COLUMN bl_is_active BOOLEAN NOT NULL DEFAULT TRUE;

UPDATE tb_users
SET bl_is_active = TRUE;
