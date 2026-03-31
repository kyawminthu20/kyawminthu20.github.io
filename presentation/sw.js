const CACHE = 'presentation-v1';
const ASSETS = [
  './',
  './index.html',
  './468228856_10160549878036191_3361227329675042348_n.jpg',
  './471329808_10160786441396191_8741166105896115896_n.jpg',
  './471541958_10160790196401191_1204109319553760719_n.jpg',
  './557707235_10161783188496191_3906616720222017376_n.jpg',
  './557786637_10161783188941191_4660828285569555597_n.jpg',
  './558984853_10161783188606191_810784134374239134_n.jpg',
];

self.addEventListener('install', e => {
  e.waitUntil(
    caches.open(CACHE).then(c => c.addAll(ASSETS)).then(() => self.skipWaiting())
  );
});

self.addEventListener('activate', e => {
  e.waitUntil(
    caches.keys().then(keys =>
      Promise.all(keys.filter(k => k !== CACHE).map(k => caches.delete(k)))
    ).then(() => self.clients.claim())
  );
});

self.addEventListener('fetch', e => {
  e.respondWith(
    caches.match(e.request).then(cached => cached || fetch(e.request))
  );
});
