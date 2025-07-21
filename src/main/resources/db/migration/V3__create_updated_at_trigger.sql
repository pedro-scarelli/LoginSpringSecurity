CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.dt_updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER tr_person_updated_at BEFORE UPDATE ON tb_person 
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER tr_company_updated_at BEFORE UPDATE ON tb_company 
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER tr_user_updated_at BEFORE UPDATE ON tb_user 
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER tr_client_updated_at BEFORE UPDATE ON tb_client 
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER tr_appointment_updated_at BEFORE UPDATE ON tb_appointment 
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE OR REPLACE FUNCTION set_appointment_completed_at()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.st_status = 'COMPLETED' AND OLD.st_status != 'COMPLETED' THEN
        NEW.dt_completed_at = CURRENT_TIMESTAMP;
    ELSIF NEW.st_status != 'COMPLETED' THEN
        NEW.dt_completed_at = NULL;
    END IF;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER tr_appointment_completed_at BEFORE UPDATE ON tb_appointment 
    FOR EACH ROW EXECUTE FUNCTION set_appointment_completed_at();