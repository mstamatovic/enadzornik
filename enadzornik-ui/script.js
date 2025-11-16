// Učitaj škole na stranici za registraciju
if (document.getElementById('skolaId')) {
    axios.get('http://localhost:8083/api/v1/auth/skola')
        .then(response => {
            const select = document.getElementById('skolaId');
            response.data.forEach(skola => {
                const option = document.createElement('option');
                option.value = skola.skolaId;
                option.textContent = skola.skolaNaziv;
                select.appendChild(option);
            });
        })
        .catch(err => {
            console.error('Greška pri učitavanju škola:', err);
            document.getElementById('message').textContent = 'Greška pri učitavanju škola.';
        });
}

// Registracija
if (document.getElementById('registerForm')) {
    document.getElementById('registerForm').addEventListener('submit', async (e) => {
        e.preventDefault();
        const data = {
            ime: document.getElementById('ime').value,
            prezime: document.getElementById('prezime').value,
            email: document.getElementById('email').value,
            password: document.getElementById('password').value,
            uloga: document.getElementById('uloga').value,
            skolaId: parseInt(document.getElementById('skolaId').value)
        };

        try {
            await axios.post('http://localhost:8083/api/v1/auth/register', data);
            document.getElementById('message').textContent = 'Uspešno registrovan!';
            document.getElementById('registerForm').reset();
        } catch (err) {
            document.getElementById('message').textContent = 'Greška: ' + (err.response?.data?.message || err.message);
        }
    });
}

// Login
if (document.getElementById('loginForm')) {
    document.getElementById('loginForm').addEventListener('submit', async (e) => {
        e.preventDefault();
        const data = {
            email: document.getElementById('email').value,
            password: document.getElementById('password').value
        };

        try {
            const res = await axios.post('http://localhost:8083/api/v1/auth/login', data);
            const token = res.data.token;
            document.getElementById('message').textContent = 'Uspešno prijavljen!';
            document.getElementById('token').innerHTML = `<strong>JWT Token:</strong> <br>${token}`;
            // Opciono: sačuvaj u localStorage za dalju upotrebu
            localStorage.setItem('jwtToken', token);
        } catch (err) {
            document.getElementById('message').textContent = 'Greška: ' + (err.response?.data?.message || err.message);
        }
    });
}