-- Insert data for chemical, food, and petro companies in the Netherlands
INSERT INTO company (name, description, location) VALUES ('NexGen Chemicals', 'Innovative chemical manufacturer leading in sustainable solutions.', 'Amsterdam');
INSERT INTO company (name, description, location) VALUES ('TasteBud Delicacies', 'Artisanal food producer offering a wide range of gourmet delights.', 'Rotterdam');
INSERT INTO company (name, description, location) VALUES ('GreenPetro', 'Environmentally conscious energy company specializing in petrochemical products.', 'The Hague');
INSERT INTO company (name, description, location) VALUES ('Healthy Harvest', 'Health-focused food supplier providing organic and nutritious snacks.', 'Utrecht');
INSERT INTO company (name, description, location) VALUES ('PetroEvo', 'Cutting-edge petrochemical firm revolutionizing the energy sector.', 'Eindhoven');

INSERT INTO process (name, description, product, max_process_temperature, raw_material, power, operating_hours, company_id)
VALUES ('Advanced Chemical Synthesis', 'Process for synthesizing advanced chemicals', 'Advanced Chemicals', 180.0, 'Raw Material X', 80.0, 18, 1);

INSERT INTO process (name, description, product, max_process_temperature, raw_material, power, operating_hours, company_id)
VALUES ('Polymer Blend Production', 'Process for blending polymers', 'Blended Polymers', 160.0, 'Raw Material Y', 180.0, 14, 2);

INSERT INTO process (name, description, product, max_process_temperature, raw_material, power, operating_hours, company_id)
VALUES ('Chemical Recycling', 'Process for recycling chemical byproducts', 'Recycled Chemicals', 140.0, 'Chemical Byproducts', 40.0, 10, 2);

INSERT INTO process (name, description, product, max_process_temperature, raw_material, power, operating_hours, company_id)
VALUES ('Artisan Cake Baking', 'Process for baking artisan cakes', 'Artisan Cakes', 230.0, 'Flour, Sugar, Eggs', 45.0, 8, 3);

INSERT INTO process (name, description, product, max_process_temperature, raw_material, power, operating_hours, company_id)
VALUES ('Fruit Juice Extraction', 'Process for extracting fruit juice', 'Fresh Fruit Juice', 100.0, 'Fresh Fruits', 60.0, 16, 3);
INSERT INTO process (name, description, product, max_process_temperature, raw_material, power, operating_hours, company_id)
VALUES ('Organic Waste Composting', 'Process for composting organic waste', 'Composted Organic Fertilizer', 60.0, 'Organic Waste', 25.0, 6, 3);

INSERT INTO process (name, description, product, max_process_temperature, raw_material, power, operating_hours, company_id)
VALUES ('Crude Oil Refining', 'Process for crude oil refining', 'Refined Petroleum Products', 280.0, 'Crude Oil', 450.0, 20, 4);
INSERT INTO process (name, description, product, max_process_temperature, raw_material, power, operating_hours, company_id)
VALUES ('Natural Gas Compression', 'Process for compressing natural gas', 'Compressed Natural Gas (CNG)', 350.0, 'Natural Gas', 700.0, 22, 5);
