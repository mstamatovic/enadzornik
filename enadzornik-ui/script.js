// === SAMO JEDNOM UČITAJ DOM ===
document.addEventListener('DOMContentLoaded', () => {

    // === LOGIN ===
    if (document.getElementById('loginForm')) {
        document.getElementById('loginForm').addEventListener('submit', async (e) => {
            e.preventDefault();
            const {email, password} = {
                email: document.getElementById('email').value.trim(),
                password: document.getElementById('password').value
            };
            const msg = document.getElementById('message');

            try {
                const res = await fetch('http://localhost:8083/api/v1/auth/login', {
                    method: 'POST',
                    headers: {'Content-Type': 'application/json'},
                    body: JSON.stringify({email, password})
                });

                const data = await res.json();
                if (res.ok && data.token) {
                    localStorage.setItem('jwtToken', data.token);
                    const payload = JSON.parse(atob(data.token.split('.')[1]));
                    const role = payload.role;
                    const displayName = (payload.ime || '') + ' ' + (payload.prezime || '');
                    localStorage.setItem('korisnikImePrezime', displayName.trim() || 'Korisnik');

                    const pages = {
                        'admin': 'dashboard/admin.html',
                        'nastavnik': 'dashboard/nastavnik.html',
                        'supervizor': 'dashboard/supervizor.html'
                    };

                    window.location.href = pages[role] || 'index.html';
                } else {
                    msg.textContent = data.message || 'Pogrešan email/lozinka';
                }
            } catch (err) {
                msg.textContent = 'Mrežna greška';
                console.error(err);
            }
        });
    }

    // === REGISTRACIJA ===
    if (document.getElementById('registerForm')) {
        // Učitaj škole
        fetch('http://localhost:8083/api/v1/auth/skola')
            .then(res => res.json())
            .then(skole => {
                const sel = document.getElementById('skolaId');
                skole.forEach(s => {
                    const opt = document.createElement('option');
                    opt.value = s.skolaId;
                    opt.textContent = s.skolaNaziv;
                    sel.appendChild(opt);
                });
            });

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
            const msg = document.getElementById('message');

            try {
                const res = await fetch('http://localhost:8083/api/v1/auth/register', {
                    method: 'POST',
                    headers: {'Content-Type': 'application/json'},
                    body: JSON.stringify(data)
                });

                const result = await res.json();
                msg.style.color = res.ok ? 'green' : 'red';
                msg.textContent = res.ok ? 'Uspešno registrovan!' : (result.message || 'Greška');
            } catch (err) {
                msg.style.color = 'red';
                msg.textContent = 'Mrežna greška';
            }
        });
    }
});