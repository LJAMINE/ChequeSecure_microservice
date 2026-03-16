-- Données initiales pour le scénario de test (chèque à certifier)
INSERT INTO cheques (numero_cheque, code_banque, numero_compte, cle_rib, nom_client, montant, statut, date_creation)
SELECT 'CHQ-TEST-001', '011', '12345678901', '12', 'Client Test', 500.0, 'EN_ATTENTE', CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM cheques WHERE numero_cheque = 'CHQ-TEST-001');
