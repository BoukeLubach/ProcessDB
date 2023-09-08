-- Insert data for chemical, food, and petro companies in the Netherlands
INSERT INTO company (name, description, location) VALUES ('Acme Chemicals', 'Leading chemical manufacturer specializing in innovative solutions.', 'Amsterdam');
INSERT INTO company (name, description, location) VALUES ('Gourmet Delights', 'Premium food producer offering exquisite culinary creations.', 'Rotterdam');
INSERT INTO company (name, description, location) VALUES ('EcoPetro', 'Sustainable energy company providing eco-friendly petrochemical products.', 'The Hague');
INSERT INTO company (name, description, location) VALUES ('Tasty Treats', 'Artisanal food supplier delivering delicious and healthy snacks.', 'Utrecht');
INSERT INTO company (name, description, location) VALUES ('PetroEx', 'Pioneering petrochemical firm revolutionizing energy solutions.', 'Eindhoven');

INSERT INTO process (name, description, product, max_process_temperature, raw_material, power, operating_hours, company_id)
VALUES ('Specialty Chemical Synthesis', 'Process for synthesizing specialty chemicals', 'Specialty Chemicals', 200.0, 'Raw Material A', 100.0, 24, 1);
INSERT INTO process (name, description, product, max_process_temperature, raw_material, power, operating_hours, company_id)
VALUES ('Polymer Production', 'Process for polymerization of plastics', 'Plastic Polymers', 180.0, 'Raw Material B', 200.0, 16, 1);
INSERT INTO process (name, description, product, max_process_temperature, raw_material, power, operating_hours, company_id)
VALUES ('Chemical Waste Treatment', 'Process for treating chemical waste', 'Treated Chemical Waste', 150.0, 'Chemical Waste', 50.0, 8, 1);

INSERT INTO process (name, description, product, max_process_temperature, raw_material, power, operating_hours, company_id)
VALUES ('Artisan Bread Baking', 'Process for baking artisan bread', 'Artisan Bread', 250.0, 'Flour, Yeast', 50.0, 10, 2);
INSERT INTO process (name, description, product, max_process_temperature, raw_material, power, operating_hours, company_id)
VALUES ('Dairy Product Pasteurization', 'Process for dairy product pasteurization', 'Pasteurized Dairy Products', 120.0, 'Milk, Cream', 75.0, 12, 2);
INSERT INTO process (name, description, product, max_process_temperature, raw_material, power, operating_hours, company_id)
VALUES ('Food Waste Recycling', 'Process for recycling food waste', 'Recycled Food Materials', 80.0, 'Food Waste', 30.0, 6, 2);

INSERT INTO process (name, description, product, max_process_temperature, raw_material, power, operating_hours, company_id)
VALUES ('Crude Oil Distillation', 'Process for crude oil distillation', 'Distilled Petroleum Products', 300.0, 'Crude Oil', 500.0, 24, 3);
INSERT INTO process (name, description, product, max_process_temperature, raw_material, power, operating_hours, company_id)
VALUES ('Liquefied Natural Gas Production', 'Process for liquefying natural gas', 'Liquefied Natural Gas (LNG)', 400.0, 'Natural Gas', 800.0, 18, 3);
