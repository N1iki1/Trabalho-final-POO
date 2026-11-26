-- =====================================================
-- Projeto: Neuraxi - Gestão de Pacientes e Agendamentos
-- Banco: SQLite
-- Duas tabelas relacionadas: paciente (1) e agendamento (N)
-- =====================================================

CREATE TABLE IF NOT EXISTS paciente (
    id_paciente     INTEGER PRIMARY KEY AUTOINCREMENT,
    nome            TEXT    NOT NULL,
    telefone        TEXT    NOT NULL,
    data_nascimento TEXT    NOT NULL           -- formato dd/mm/aaaa
);

CREATE TABLE IF NOT EXISTS agendamento (
    id_agendamento  INTEGER PRIMARY KEY AUTOINCREMENT,
    id_paciente     INTEGER NOT NULL,
    servico         TEXT    NOT NULL,
    data_hora       TEXT    NOT NULL,           -- formato dd/mm/aaaa hh:mm
    status          TEXT    NOT NULL DEFAULT 'pedido'
                    CHECK (status IN ('pedido','confirmado','realizado','cancelado')),
    CONSTRAINT fk_agendamento_paciente FOREIGN KEY (id_paciente)
        REFERENCES paciente (id_paciente) ON DELETE RESTRICT
);
