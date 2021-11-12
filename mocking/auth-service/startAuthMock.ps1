Write-Output "--- Kill du conteneur"
docker kill amt-mock-auth

Write-Output "--- Retrait du conteneur"
docker rm amt-mock-auth

# Vérification de l'existence de l'image
$existImage = docker images -q crossport/amt-mock-auth
if($null -eq $existImage)
{
    Write-Output "--- Build image"
    docker build -t crossport/amt-mock-auth .
    
}

# Démarrage des conteneurs
Write-Output "--- Demarrage du conteneur"
docker run -d -p 8081:8081 --name amt-mock-auth crossport/amt-mock-auth
