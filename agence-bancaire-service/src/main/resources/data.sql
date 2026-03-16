-- Données initiales pour le scénario de test (compte 12345678901 avec solde 5000 DH)
INSERT INTO comptes (numero_compte, solde, reference_client, nom_client, type, date_ouverture, actif)
SELECT '12345678901', 5000.0, 'REF001', 'Client Test', 'COURANT', CURRENT_DATE, true
WHERE NOT EXISTS (SELECT 1 FROM comptes WHERE numero_compte = '12345678901');
