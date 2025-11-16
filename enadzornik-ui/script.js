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

    // Dekodiraj JWT token (bez validacije potpisa – samo za UI)
    function parseJwt(token) {
        const base64Url = token.split('.')[1];
        const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
        const jsonPayload = decodeURIComponent(atob(base64).split('').map(c => {
            return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
        }).join(''));
        return JSON.parse(jsonPayload);
    }

// Sačuvaj token i preusmeri na odgovarajuću stranicu
    function handleLogin(token) {
        localStorage.setItem('jwtToken', token);
        const payload = parseJwt(token);
        const role = payload.role;
        const imePrezime = `${payload.ime} ${payload.prezime}`;

        // Sačuvaj i za prikaz
        localStorage.setItem('korisnikImePrezime', imePrezime);

        // Preusmeri na odgovarajuću stranicu
        let dashboard;
        switch (role) {
            case 'admin':
                dashboard = 'dashboard/admin.html';
                break;
            case 'supervizor':
                dashboard = 'dashboard/supervizor.html';
                break;
            case 'nastavnik':
                dashboard = 'dashboard/nastavnik.html';
                break;
            default:
                dashboard = 'index.html';
        }

        window.location.href = dashboard;
    }

// Postavi ime i prezime na dashboard stranicama
    function populateUserInfo() {
        const span = document.getElementById('imePrezime');
        if (span) {
            span.textContent = localStorage.getItem('korisnikImePrezime') || 'Korisnik';
        }
    }

// Logout
    function logout() {
        localStorage.removeItem('jwtToken');
        localStorage.removeItem('korisnikImePrezime');
        window.location.href = '../index.html';
    }

// Login forma
    if (document.getElementById('loginForm')) {
        document.getElementById('loginForm').addEventListener('submit', async (e) => {
            e.preventDefault();
            const data = {
                email: document.getElementById('email').value,
                password: document.getElementById('password').value
            };

            try {
                const res = await axios.post('http://localhost:8083/api/v1/auth/login', data);
                handleLogin(res.data.token);
            } catch (err) {
                const msg = document.getElementById('message');
                if (msg) {
                    msg.textContent = 'Greška: ' + (err.response?.data?.message || err.message);
                }
            }
        });
    }

// Učitavanje škola (samo na register.html)
//     if (document.getElementById('skolaId')) {
//         axios.get('http://localhost:8081/api/v1/auth/skole')
//             .then(response => {
//                 const select = document.getElementById('skolaId');
//                 response.data.forEach(skola => {
//                     const option = document.createElement('option');
//                     option.value = skola.skolaId;
//                     option.textContent = skola.skolaNaziv;
//                     select.appendChild(option);
//                 });
//             })
//             .catch(err => {
//                 console.error('Greška pri učitavanju škola:', err);
//                 const msg = document.getElementById('message');
//                 if (msg) {
//                     msg.textContent = 'Greška pri učitavanju škola.';
//                 }
//             });
//     }

// Na dashboard stranicama, prikaži ime korisnika
    if (window.location.pathname.includes('dashboard/')) {
        populateUserInfo();
    }
}