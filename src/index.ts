import { registerPlugin } from '@capacitor/core';

import type { FloatingWindowPlugin } from './definitions';

const FloatingWindow = registerPlugin<FloatingWindowPlugin>('FloatingWindow', {
  android: () => import('./android').then(m => new m.FloatingWindowAndroid()),
});

export * from './definitions';
export { FloatingWindow };
