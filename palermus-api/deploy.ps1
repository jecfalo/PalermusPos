# Script para desplegar la API a Google Cloud Run automáticamente

echo "Compilando y subiendo nueva imagen a Google Cloud Build..."
gcloud builds submit --tag us-central1-docker.pkg.dev/project-16f1e4a3-0919-4ec2-bd1/backend-repo/backend-api:latest

echo "Desplegando la nueva imagen en Cloud Run usando Secret Manager..."
gcloud run deploy palermus-backend `
  --image us-central1-docker.pkg.dev/project-16f1e4a3-0919-4ec2-bd1/backend-repo/backend-api:latest `
  --region southamerica-east1 `
  --allow-unauthenticated `
  --set-env-vars="URL_DB=jdbc:postgresql://34.176.17.216:5432/palermus_db,NAME_DB=postgres" `
  --set-secrets="PASSWORD_DB=palermus-db-password:latest,JWT_SECRET=palermus-jwt-secret:latest,ENCRYPTION=palermus-encryption-key:latest"
echo "¡Despliegue finalizado!"
