-- Données initiales pour le scénario de test (agence 011)
-- Pré-création de l'agence pour éviter "Agence non trouvée ou inactive"
INSERT INTO agences_bancaires (nom, ville, adresse, code_banque, url_service_web, email, telephone, actif)
SELECT 'Agence Casablanca Centre', 'Casablanca', '123 Bd Mohammed V', '011', 'http://localhost:8083', 'agence011@bank.ma', '+212522123456', true
WHERE NOT EXISTS (SELECT 1 FROM agences_bancaires WHERE code_banque = '011');
