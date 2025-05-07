ALTER TABLE tb_users
  ADD COLUMN bl_is_enabled BOOLEAN NOT NULL DEFAULT TRUE;

UPDATE tb_users
SET bl_is_enabled = TRUE;
