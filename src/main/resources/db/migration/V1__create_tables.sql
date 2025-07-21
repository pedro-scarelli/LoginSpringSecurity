CREATE TYPE person_type AS ENUM ('USER', 'CLIENT');
CREATE TYPE appointment_status AS ENUM ('SCHEDULED', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED');

CREATE TABLE tb_person (
    pk_id UUID PRIMARY KEY,
    st_person_type person_type NOT NULL,
    st_name VARCHAR(255) NOT NULL,
    st_cpf VARCHAR(11) NOT NULL,
    st_email VARCHAR(255),
    st_phone VARCHAR(20),
    fk_address_id UUID NOT NULL,
    dt_created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    dt_updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    dt_deleted_at TIMESTAMP WITH TIME ZONE,
    
    CONSTRAINT ck_person_email CHECK (st_email IS NULL OR st_email ~ '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$'),
    CONSTRAINT ck_person_document CHECK (LENGTH(st_cpf) = 11)
);


CREATE TABLE tb_address (
	pk_id UUID PRIMARY KEY,
    st_street VARCHAR(500) NOT NULL,
    it_number SMALLINT NOT NULL,
    st_complement VARCHAR(255),
    st_neighborhood VARCHAR(255) NOT NULL,
    st_city VARCHAR(255) NOT NULL,
    st_state VARCHAR(2) NOT NULL,
    st_zip_code VARCHAR(10) NOT NULL,
    dt_deleted_at TIMESTAMP WITH TIME ZONE,
    st_country VARCHAR(100) NOT NULL
);

CREATE TABLE tb_company (
    pk_id UUID PRIMARY KEY,
    st_legal_name VARCHAR(255) NOT NULL,
    st_trade_name VARCHAR(255),
    st_cnpj VARCHAR(14) NOT NULL,
    st_state_registration VARCHAR(50),
    st_municipal_registration VARCHAR(50),
    
    dc_default_hourly_rate DECIMAL(10,2),
    
    st_tax_regime VARCHAR(50),
    st_municipal_service_code VARCHAR(20),
    
    fk_user_id UUID NOT NULL,
    
    dt_created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    dt_updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    dt_deleted_at TIMESTAMP WITH TIME ZONE,
    
    CONSTRAINT ck_company_document CHECK (LENGTH(st_cnpj) = 14),
    CONSTRAINT ck_company_hourly_rate CHECK (dc_default_hourly_rate IS NULL OR dc_default_hourly_rate > 0)
);

CREATE TABLE tb_user (
    pk_id UUID PRIMARY KEY,
    st_password_hash VARCHAR(255) NOT NULL,
    bl_enabled BOOLEAN DEFAULT FALSE,
    it_role SMALLINT NOT NULL,
    st_otp_code VARCHAR(6),
    dt_otp_timestamp TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    dt_created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    dt_updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    dt_deleted_at TIMESTAMP WITH TIME ZONE,
    CONSTRAINT fk_user_person FOREIGN KEY (pk_id) REFERENCES tb_person(pk_id) ON DELETE CASCADE
);

CREATE TABLE tb_client (
    pk_id UUID PRIMARY KEY,
    fk_company_id UUID NOT NULL,
    
    dt_created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    dt_updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    dt_deleted_at TIMESTAMP WITH TIME ZONE,
    
    CONSTRAINT fk_client_person FOREIGN KEY (pk_id) REFERENCES tb_person(pk_id) ON DELETE CASCADE,
    CONSTRAINT fk_client_company FOREIGN KEY (fk_company_id) REFERENCES tb_company(pk_id) ON DELETE CASCADE
);

CREATE TABLE tb_appointment (
    pk_id UUID PRIMARY KEY,
    fk_client_id UUID NOT NULL,
    fk_company_id UUID NOT NULL,
    fk_address_id UUID NOT NULL,
    
    dt_scheduled_date TIMESTAMP WITH TIME ZONE NOT NULL,
    dc_estimated_duration_hours DECIMAL(4,2),
    dc_actual_duration_hours DECIMAL(4,2),
    
    st_service_description TEXT NOT NULL,
    
    dc_estimated_amount DECIMAL(10,2),
    dc_final_amount DECIMAL(10,2),
    
    st_status appointment_status DEFAULT 'SCHEDULED',
    st_notes TEXT,
    
    st_nfe_number VARCHAR(20),
    st_nfe_key VARCHAR(100),
    dt_nfe_issued_at TIMESTAMP WITH TIME ZONE,
    
    st_google_calendar_event_id VARCHAR(255),
    
    dt_created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    dt_updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    dt_completed_at TIMESTAMP WITH TIME ZONE,
    dt_deleted_at TIMESTAMP WITH TIME ZONE,
    
    CONSTRAINT ck_appointment_amounts CHECK (
        (dc_estimated_amount IS NULL OR dc_estimated_amount >= 0) AND
        (dc_final_amount IS NULL OR dc_final_amount >= 0)
    ),
    CONSTRAINT ck_appointment_duration CHECK (
        (dc_estimated_duration_hours IS NULL OR dc_estimated_duration_hours > 0) AND
        (dc_actual_duration_hours IS NULL OR dc_actual_duration_hours > 0)
    ),
    CONSTRAINT ck_appointment_completed_status CHECK (
        (st_status != 'COMPLETED') OR (dt_completed_at IS NOT NULL AND dc_final_amount IS NOT NULL)
    ),
    
    CONSTRAINT fk_appointment_client FOREIGN KEY (fk_client_id) REFERENCES tb_client(pk_id) ON DELETE RESTRICT,
    CONSTRAINT fk_appointment_company FOREIGN KEY (fk_company_id) REFERENCES tb_company(pk_id) ON DELETE CASCADE,
    CONSTRAINT fk_appointment_address FOREIGN KEY (fk_address_id) REFERENCES tb_address(pk_id) ON DELETE RESTRICT
);

ALTER TABLE tb_company 
    ADD CONSTRAINT fk_company_owner 
    FOREIGN KEY (fk_user_id) REFERENCES tb_user(pk_id) ON DELETE RESTRICT;

ALTER TABLE tb_person
    ADD CONSTRAINT fk_person_address_ref 
    FOREIGN KEY (fk_address_id) REFERENCES tb_address(pk_id) ON DELETE SET NULL;