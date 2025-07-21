CREATE INDEX idx_person_document ON tb_person(st_cpf);
CREATE INDEX idx_person_email ON tb_person(st_email) WHERE st_email IS NOT NULL;
CREATE INDEX idx_person_type ON tb_person(st_person_type);

CREATE INDEX idx_company_document ON tb_company(st_cnpj);
CREATE INDEX idx_company_owner ON tb_company(fk_user_id);

CREATE INDEX idx_user_active ON tb_user(bl_enabled) WHERE bl_enabled = TRUE;

CREATE INDEX idx_client_company ON tb_client(fk_company_id);

CREATE INDEX idx_appointment_client ON tb_appointment(fk_client_id);
CREATE INDEX idx_appointment_company ON tb_appointment(fk_company_id);
CREATE INDEX idx_appointment_scheduled_date ON tb_appointment(dt_scheduled_date);
CREATE INDEX idx_appointment_status ON tb_appointment(st_status);
CREATE INDEX idx_appointment_company_date ON tb_appointment(fk_company_id, dt_scheduled_date);

CREATE INDEX idx_person_name_lower ON tb_person(LOWER(st_name));
